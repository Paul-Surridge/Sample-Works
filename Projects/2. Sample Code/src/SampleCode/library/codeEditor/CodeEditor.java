/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode.library.codeEditor;

import SampleCode.library.Library;
import SampleCode.shared.*;
import static SampleCode.shared.Constants.SEARCH_HIGHLIGHT_NEXT;
import static SampleCode.shared.Constants.SEARCH_HIGHLIGHT_PREVIOUS;
import static SampleCode.shared.Constants.TAB_PANE_CODE;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

/**
 *
 * @author PaulSu
 */
public class CodeEditor implements Constants {

    private final Library           objLibrary;
    private final TabPane           tpViewer;
    private final AnchorPane        apFullScreen;
    private final TextArea          taCode, taAsciiTable, taEscapeSequences, taInstructions, taFullScreen;
    private final TextField         tfHeader;
    private final Button            btnSearchPrevious;
    private final Button            btnSearchNext;
    private final Button            btnSearchPreviousFullScreen;
    private final Button            btnSearchNextFullScreen;
    private final Label             labSearchNumberOfMatches;
    private final Label             labSearchNumberOfMatchesFullScreen;
    
    private String                  sSelectedText, sAutoIndentationCharacter,
                                    sEscapeSequences, sInstructions, sAsciiTable;
    
    private int                     iExtendedAnchorPosition,
                                    iExtendedCaretPosition,
                                    iTabWhiteSpaceCulumativeDifferenceTotal,
                                    iAutoIndentationLength,
                                    iSearchHighlightCounter;
    
    private boolean                 bCodeChanged;
    
    private ArrayList               alSearchPositions = new ArrayList<SearchItem>();
    private ArrayList               alTextAreas = new ArrayList<TextArea>();

    public CodeEditor(Library objLibrary, TabPane tpViewer, TextField tfHeader, TextArea taCode,
                        Button btnSearchPrevious, Button btnSearchNext,
                        Button btnSearchPreviousFullScreen, Button btnSearchNextFullScreen,
                        Label labSearchNumberOfMatches, Label labSearchNumberOfMatchesFullScreen,
                        AnchorPane apFullScreen, TextArea taFullScreen,
                        TextArea taAsciiTable, TextArea taEscapeSequences, TextArea taInstructions)
    {
        this.objLibrary                         = objLibrary;
        this.tpViewer                           = tpViewer;
        this.taCode                             = taCode;
        this.tfHeader                           = tfHeader;
        this.apFullScreen                       = apFullScreen;
        this.taFullScreen                       = taFullScreen;
        
        this.btnSearchPrevious                  = btnSearchPrevious;
        this.btnSearchNext                      = btnSearchNext;
        this.btnSearchPreviousFullScreen        = btnSearchPreviousFullScreen;
        this.btnSearchNextFullScreen            = btnSearchNextFullScreen;
        
        this.labSearchNumberOfMatches           = labSearchNumberOfMatches;
        this.labSearchNumberOfMatchesFullScreen = labSearchNumberOfMatchesFullScreen;
        
        this.taAsciiTable                       = taAsciiTable;
        this.taEscapeSequences                  = taEscapeSequences;
        this.taInstructions                     = taInstructions;
        
        alTextAreas.add(taCode);
        alTextAreas.add(taAsciiTable);
        alTextAreas.add(taEscapeSequences);
        alTextAreas.add(taInstructions);
        
        loadExternalFiles();
        refreshTextAreaCSSStyle();
        clearTextAreaContextMenus();
        
        initButtonTooltips();
        initMouseEventHandlers();
        initKeyEventHandlers();
        initKeyEventHandlersFullScreen();
        initEventListeners();
        
        taFullScreen.setText("");
        taCode.setText("Please Select Node From Library");
    }
    
    
    //Text Manipulation
    private int getAnchorPosition()
    {
        return taCode.getAnchor();
    }
    private int getCaretPosition()
    {
        return taCode.getCaretPosition();
    }
    private void setCaretPosition(int iPosition)
    {
        taCode.positionCaret(iPosition);
    }
    private int findSizeOfSelectedText()
    {
        return taCode.getSelectedText().length();
    }
    private int getLengthOfTextAreaText()
    {
        return taCode.getText().length();
    }
    private void refreshSelectedRange()
    {
        taCode.selectRange(iExtendedAnchorPosition, iExtendedCaretPosition + iTabWhiteSpaceCulumativeDifferenceTotal);
    }
    private void resetTabWhiteSpaceCulumativeDifferenceTotal()
    {
        iTabWhiteSpaceCulumativeDifferenceTotal = 0;
    }
    private String removeWhitespacePrefixFromLine(String sLine, int iAmount)
    {
        iTabWhiteSpaceCulumativeDifferenceTotal = iTabWhiteSpaceCulumativeDifferenceTotal - iAmount;
        
        return sLine.substring(iAmount, sLine.length());
    }
    private String buildBlankLine(int iAmount)
    {
        String sBuild;
        
        sBuild = "";
        
        for(int i = 1 ; i<=iAmount ; i++)
            sBuild = sBuild + " ";
        
        return sBuild;
    }
    private String insertWhitespacePrefixToLine(String sLine, int iAmount)
    {
        iTabWhiteSpaceCulumativeDifferenceTotal = iTabWhiteSpaceCulumativeDifferenceTotal + iAmount;
        
        return buildBlankLine(iAmount) + sLine;
    }
    private int findLengthOfWhitespaceAtStartOfLine(String sLine)
    {
        int iCount;

        iCount = 0;

        while(  (iCount<(sLine.length())) && (sLine.substring(iCount, (iCount+1)).equals(" ")) )
            iCount++;
        
        return iCount;
    }
    private int findLengthOfWhitespaceAtStartOfLine(int iPosition)
    {
        int iCount, iStartPosition;

        iStartPosition  = findPositionAtStartOfLine(iPosition);

        iCount = 0;

        while(  (iCount < getLengthOfTextAreaText()) && (getNextCharacterInTextArea(iStartPosition + iCount).equals(" ")) )
            iCount++;
        
        return iCount;
    }
    private String tabLine(String sLine, int iDirection)
    {
        int iLengthOfWhiteSpaceAtStartOfLine, iTabOffset;
        
        iLengthOfWhiteSpaceAtStartOfLine = findLengthOfWhitespaceAtStartOfLine(sLine);

        switch(iDirection)
        {
            case TAB_FORWARD:
            {
                iTabOffset = TAB_SIZE - (iLengthOfWhiteSpaceAtStartOfLine % TAB_SIZE);
                
                if(iTabOffset == 0)     return insertWhitespacePrefixToLine(sLine, TAB_SIZE);
                else                    return insertWhitespacePrefixToLine(sLine, iTabOffset);
            }
            case TAB_BACKWARD:
            {
                if(iLengthOfWhiteSpaceAtStartOfLine > 0)
                {
                    iTabOffset = iLengthOfWhiteSpaceAtStartOfLine % TAB_SIZE;

                    if(iTabOffset == 0) return removeWhitespacePrefixFromLine(sLine, TAB_SIZE);
                    else                return removeWhitespacePrefixFromLine(sLine, iTabOffset);
                }
            }
        }
        
        return sLine;
    }
    private String[] tabSelectedText(String sSelectedText, int iDirection)
    { 
        String[] sLines;

        sLines = sSelectedText.split(newLine());
        
        for(int i=0; i<sLines.length ; i++)
        {
            sLines[i] = tabLine(sLines[i], iDirection);
            
            if(i < (sLines.length - 1))
                sLines[i] = sLines[i] + newLine();
        }

        return sLines;
    }
    private void insertStringArrayIntoTextArea(String[] sLines)
    {
        for(String s : sLines)
            taCode.insertText(getCaretPosition(), s);
    }
    private void insertStringIntoTextArea(String sLine, int iPosition)
    {
        taCode.insertText(iPosition, sLine);
    }
    private void removeStringFromTextArea(int iStartPosition, int iEndPosition)
    {
        taCode.deleteText(iStartPosition, iEndPosition);
    }
    private String getPreviousCharacterInTextArea(int iPostion)
    {
        if(iPostion > 0)    return taCode.getText((iPostion-1), (iPostion));
        else                return "";
    }
    private String getNextCharacterInTextArea(int iPostion)
    {
        if(iPostion < getLengthOfTextAreaText())    return taCode.getText((iPostion), (iPostion+1));
        else                                        return "";
    }
    private int findPositionAtStartOfLine(int iPosition)
    {
        int iStartOfLinePosition;
        
        iStartOfLinePosition = iPosition;
        
        if(iStartOfLinePosition == 0)
            return 0;
        else
        {
            while ( (iStartOfLinePosition > 0) && (!getPreviousCharacterInTextArea(iStartOfLinePosition).equals("\n")) )
                iStartOfLinePosition--;
        }
        
        return iStartOfLinePosition;
    }
    private int findPositionAtEndOfLine(int iPosition)
    {
        int iEndOfLinePosition;
        
        iEndOfLinePosition = iPosition;
        
        while ( (iEndOfLinePosition < getLengthOfTextAreaText()) && (!getNextCharacterInTextArea(iEndOfLinePosition).equals("\n")) )
            iEndOfLinePosition++;
        
        return iEndOfLinePosition;
    }
    private void extendSelectedTextRangeToContainFullLines()
    {
        int iAnchorPosition, iCaretPosition;
        
        iAnchorPosition     = getAnchorPosition();
        iCaretPosition      = getCaretPosition();
        
        if(iAnchorPosition == iCaretPosition)
        {
            iExtendedAnchorPosition = findPositionAtStartOfLine(iCaretPosition);
            iExtendedCaretPosition  = findPositionAtEndOfLine(iCaretPosition);
        }
        else if(iAnchorPosition < iCaretPosition)
        {
            iExtendedAnchorPosition = findPositionAtStartOfLine(iAnchorPosition);
            iExtendedCaretPosition  = findPositionAtEndOfLine(iCaretPosition);
        }
        else
        {
            iExtendedAnchorPosition = findPositionAtStartOfLine(iCaretPosition);
            iExtendedCaretPosition  = findPositionAtEndOfLine(iAnchorPosition);
        }
        
        taCode.selectRange(iExtendedAnchorPosition, iExtendedCaretPosition);
        
        sSelectedText = taCode.getSelectedText();
    }
    private int findLengthOfLineToCaretPosition()
    {
        int iNumberOfCharacters, iCharacterEvalutionPosition;
        
        iNumberOfCharacters = 0;
        iCharacterEvalutionPosition = getCaretPosition();

        if(iCharacterEvalutionPosition == 0)
            return 0;
        else
        {
            while ( (iCharacterEvalutionPosition > 0) && (!getPreviousCharacterInTextArea(iCharacterEvalutionPosition).equals("\n")) )
            {
                iNumberOfCharacters++;
                iCharacterEvalutionPosition--;
            }
        }
        
        return iNumberOfCharacters;
    }
    private int findNextTabMarkerPosition()
    {
        int iTabOffset;
        
        iTabOffset = TAB_SIZE - (findLengthOfLineToCaretPosition() % TAB_SIZE);
        
        if(iTabOffset == 0)
            iTabOffset = getCaretPosition() + TAB_SIZE;
        
        return getCaretPosition() + iTabOffset;
    }
    private int findPreviousTabMarkerPosition()
    {
        int iTabOffset;
         
        iTabOffset = findLengthOfLineToCaretPosition() % TAB_SIZE;
        
        if(iTabOffset == 0)
            iTabOffset = TAB_SIZE;
        
        return getCaretPosition() - iTabOffset;
    }
    private int findPreviousEndOfTextPosition()
    {
        int iOffset, iStartOfLine;

        iOffset         = getCaretPosition();
        iStartOfLine    = findPositionAtStartOfLine(getCaretPosition());
        
        while ( (iOffset > 0) && (iOffset > iStartOfLine) && (getPreviousCharacterInTextArea(iOffset).equals(" ")) )
            iOffset--;

        return iOffset;
    }
    private void macroTabSelectedText(int iDirection)
    {
        resetTabWhiteSpaceCulumativeDifferenceTotal();
            
        extendSelectedTextRangeToContainFullLines();

        taCode.cut();

        insertStringArrayIntoTextArea(tabSelectedText(sSelectedText, iDirection));

        refreshSelectedRange();
    }
    private void btnTabTextForward()
    {
        if(findSizeOfSelectedText() == 0)       insertStringIntoTextArea(buildBlankLine(findNextTabMarkerPosition() - getCaretPosition()), getCaretPosition());
        else                                    macroTabSelectedText(TAB_FORWARD);  
    }
    private void btnTabTextBackward()
    {
        int iPreviousTabMarkerPosition, iPreviousEndOfTextPosition;

        if(findSizeOfSelectedText() == 0)
        {
            iPreviousTabMarkerPosition = findPreviousTabMarkerPosition();
            iPreviousEndOfTextPosition = findPreviousEndOfTextPosition();
            
            if      (iPreviousTabMarkerPosition == iPreviousEndOfTextPosition)      removeStringFromTextArea(iPreviousTabMarkerPosition, getCaretPosition());
            else if (iPreviousTabMarkerPosition > iPreviousEndOfTextPosition)       removeStringFromTextArea(iPreviousTabMarkerPosition, getCaretPosition());
            else                                                                    removeStringFromTextArea(iPreviousEndOfTextPosition, getCaretPosition());
        }
        else
        {
             macroTabSelectedText(TAB_BACKWARD);
        }
    }
    private String newLine()
    {
        return "\n";
    }
    

    //Search
    private int findNumberOfRows(String sText, int iPosition)
    {
        int iCounter = 0;
 
        if(iPosition == 0)
            return 0;
        else
        {
            while(iPosition > 0)
            {
                iPosition = sText.lastIndexOf("\n", (iPosition - 1));

                if(iPosition >= 0)  iCounter++;
                else                break;
            }

            return iCounter + 1;
        }
    }
    private void setSearchNextPreviousButtons(boolean bState)
    {
        btnSearchPrevious.setVisible(bState);
        btnSearchNext.setVisible(bState);
    }
    private void setSearchNextPreviousFullScreenButtons(boolean bState)
    {
        btnSearchPreviousFullScreen.setVisible(bState);
        btnSearchNextFullScreen.setVisible(bState);
        
        labSearchNumberOfMatchesFullScreen.setVisible(bState);
    }
    private int findSearchPositions(String[] arrSearchFragments)
    {
        String sCode = taCode.getText().toLowerCase();
        int iAnchorPosition;

        alSearchPositions.clear();

        for(String s: arrSearchFragments)
        {
            iAnchorPosition = -1;
            
            while(iAnchorPosition < sCode.length())
            {
                iAnchorPosition = sCode.indexOf(s, (iAnchorPosition + 1));

                if(iAnchorPosition >= 0)    alSearchPositions.add(new SearchItem(iAnchorPosition, findNumberOfRows(sCode, iAnchorPosition), s.length()));
                else                        break;
            }
        }
        
        alSearchPositions.sort(new Comparator<SearchItem>()
        {
            @Override
            public int compare(SearchItem o1, SearchItem o2)
            {
                if(o1.getAnchor() > o2.getAnchor())         return 1;
                else if(o1.getAnchor() == o2.getAnchor())   return 0;
                else                                        return -1;
            }
        });
        
        return alSearchPositions.size();
    }
    private void refreshSearchNumberOfMatches()
    {
        labSearchNumberOfMatches.setText("Found [" + (iSearchHighlightCounter + 1) + ":" + alSearchPositions.size() + "]");
    }
    
    
    //Tabs and Full Screen
    private void refreshTextAreaCSSStyle()
    {
        String sCSS = "-fx-control-inner-background: #2f4f4f;"
                          + "-fx-text-fill: #FFFFFF;"
                          + "-fx-highlight-fill: #000000;"
                          + "-fx-highlight-text-fill: #FFFFFF;";
        
        taCode.setStyle(sCSS);
        taAsciiTable.setStyle(sCSS);
        taEscapeSequences.setStyle(sCSS);
        taInstructions.setStyle(sCSS);
        taFullScreen.setStyle(sCSS);
    }
    private void loadExternalFiles()
    {
        sInstructions = "";
        sAsciiTable = "";
        sEscapeSequences = "";
        
        try {
            for(String s : Files.readAllLines(FileSystems.getDefault().getPath("content", "instructions.txt")))
                sInstructions = sInstructions + s + newLine();
            
            for(String s : Files.readAllLines(FileSystems.getDefault().getPath("content", "asciitable.txt")))
                sAsciiTable = sAsciiTable + s + newLine();
            
            for(String s : Files.readAllLines(FileSystems.getDefault().getPath("content", "escapesequences.txt")))
                sEscapeSequences = sEscapeSequences + s + newLine();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }
    private void refreshAsciiTable()
    {
        taAsciiTable.setText(sAsciiTable);
    }
    private void refreshEscapeSequences()
    {
        taEscapeSequences.setText(sEscapeSequences);
    }
    private void refreshInstructions()
    {
        taInstructions.setText(sInstructions);
    }
    private void syncTextAreas(int iDirection, int iPane)
    {
        TextArea ta = (TextArea)alTextAreas.get(iPane);
        double dScrollTop;
        int iSelectRangeStart;
        int iSelectRangeEnd;
                
        switch(iDirection)
        {
            case NORMAL_TO_FULL:
            {
                dScrollTop          = ta.getScrollTop();
                iSelectRangeStart   = ta.getSelection().getStart();
                iSelectRangeEnd     = ta.getSelection().getEnd();
                
                taFullScreen.setText(ta.getText());
                taFullScreen.setEditable(ta.isEditable());
                taFullScreen.selectRange(iSelectRangeStart, iSelectRangeEnd);
                taFullScreen.setScrollTop(dScrollTop);
                
                break;
            }
            case FULL_TO_NORMAL:
            {
                dScrollTop          = taFullScreen.getScrollTop();
                iSelectRangeStart   = taFullScreen.getSelection().getStart();
                iSelectRangeEnd     = taFullScreen.getSelection().getEnd();
                
                ta.setText(taFullScreen.getText());
                ta.selectRange(iSelectRangeStart, iSelectRangeEnd);
                ta.setScrollTop(dScrollTop);
                
                break;
            }
        }
    }
    
    
    //Clear Text Area Context Menus
    private void clearTextAreaContextMenus()
    {
        javafx.scene.control.ContextMenu cmEmpty = new javafx.scene.control.ContextMenu();
        cmEmpty.getItems().clear();
        
        taCode.setContextMenu(cmEmpty);
        taAsciiTable.setContextMenu(cmEmpty);
        taEscapeSequences.setContextMenu(cmEmpty);
        taInstructions.setContextMenu(cmEmpty);
    }


    //External Interface
    public Boolean isCaretWithinWhitespaceAtStartOfLine()
    {
        int iCaretPosition, iStartOfLinePosition, iLengthOfWhiteSpaceAtStartOfLine;
        
        iCaretPosition                      = getCaretPosition();
        iStartOfLinePosition                = findPositionAtStartOfLine(iCaretPosition);
        iLengthOfWhiteSpaceAtStartOfLine    = findLengthOfWhitespaceAtStartOfLine(iCaretPosition);
        
        return((iStartOfLinePosition <= iCaretPosition) && (iCaretPosition <= (iStartOfLinePosition + iLengthOfWhiteSpaceAtStartOfLine)));
    }
    public void setAutoIndentationAmount()
    {
        iAutoIndentationLength = findLengthOfWhitespaceAtStartOfLine(getCaretPosition());
        
        sAutoIndentationCharacter = getPreviousCharacterInTextArea(getCaretPosition());
    }
    public void autoIndentation()
    {
        if(sAutoIndentationCharacter.equals("{"))
            iAutoIndentationLength = iAutoIndentationLength + TAB_SIZE;
        
        insertStringIntoTextArea(newLine() + buildBlankLine(iAutoIndentationLength), getCaretPosition());
        
        if(sAutoIndentationCharacter.equals("{"))
        {
            insertStringIntoTextArea(newLine() + buildBlankLine(iAutoIndentationLength - TAB_SIZE) + "}", getCaretPosition());
            
            setCaretPosition((getCaretPosition() - iAutoIndentationLength + 2));
        }
    }
    public void setTabPane(int iPane)
    {
        tpViewer.getSelectionModel().select(iPane);
    }
    public void setText(String sText)
    {
        taCode.setText(sText);
        taFullScreen.setText(sText);
    }
    public String getText()
    {
        return taCode.getText();
    }
    public void setHeader(String sName)
    {
        tfHeader.setText(sName);
    }
    public String getHeader()
    {
        return tfHeader.getText();
    }
    public void clear()
    {
        taCode.clear();
    }
    public void setSearch(boolean bState, String[] arrSearchFragments)
    {
        if(bState)
        {
            if(findSearchPositions(arrSearchFragments) > 0)
            {
                setSearchHighlight(SEARCH_HIGHLIGHT_FIRST);

                setSearchNextPreviousButtons(BUTTONS_VISIBLE);
            }
            else
                setSearch(false, null);
        }
        else
        {
            taCode.selectRange(taCode.getAnchor(), taCode.getAnchor());
            
            setSearchNextPreviousButtons(BUTTONS_NOT_VISIBLE);
            
            labSearchNumberOfMatches.setText("");
        }
    }
    public void setSearchHighlight(int iCommand)
    {
        SearchItem objSearchItem;
        
        int iAnchor, iRow, iLength;
        
        switch(iCommand)
        {
            case SEARCH_HIGHLIGHT_FIRST:
            {
                iSearchHighlightCounter = 0;
                
                break;
            }
            case SEARCH_HIGHLIGHT_PREVIOUS:
            {
                if(iSearchHighlightCounter > 0)     iSearchHighlightCounter--;
                else                                iSearchHighlightCounter = (alSearchPositions.size() - 1);
                
                break;
            }
            case SEARCH_HIGHLIGHT_NEXT:
            {
                if(iSearchHighlightCounter < (alSearchPositions.size()-1))      iSearchHighlightCounter++;
                else                                                            iSearchHighlightCounter = 0;
                
                break;
            }
        }
        
        objSearchItem = (SearchItem)alSearchPositions.get(iSearchHighlightCounter);
        
        iAnchor     = objSearchItem.getAnchor();
        iRow        = objSearchItem.getRows();
        iLength     = objSearchItem.getLength();
        
        if(apFullScreen.isVisible())
        {
            taFullScreen.setScrollLeft(0);
            taFullScreen.selectRange(findPositionAtStartOfLine(iAnchor), iAnchor + iLength);

            if(iRow == 0)   taFullScreen.setScrollTop(0);
            else            taFullScreen.setScrollTop( (iRow * 16) - 400);
        }
        else
        {
            taCode.setScrollLeft(0);
            taCode.selectRange(findPositionAtStartOfLine(iAnchor), iAnchor + iLength);

            if(iRow == 0)   taCode.setScrollTop(0);
            else            taCode.setScrollTop( (iRow * 16) - 400);
        }
        
        refreshSearchNumberOfMatches();
    }
    public void setFullScreen(boolean bState, boolean bSearchActive)
    {
        int iPane = tpViewer.getSelectionModel().getSelectedIndex();
        
        if(iPane == TAB_PANE_CODE)
            if(bState && bSearchActive)     setSearchNextPreviousFullScreenButtons(BUTTONS_VISIBLE);
            else                            setSearchNextPreviousFullScreenButtons(BUTTONS_NOT_VISIBLE);
        else                                setSearchNextPreviousFullScreenButtons(BUTTONS_NOT_VISIBLE);
        
        if(bState)  syncTextAreas(NORMAL_TO_FULL, iPane);
        else        syncTextAreas(FULL_TO_NORMAL, iPane);
        
        apFullScreen.setVisible(bState);
        
        if(bState)
            apFullScreen.requestFocus();
    }
    public void resetCodeChangeListener()
    {
        bCodeChanged = false;
    }
    public boolean hasCodeChanged()
    {
        return bCodeChanged;
    }
    public boolean isFullScreen()
    {
        return apFullScreen.isVisible();
    }

    //Event Handler and Listeners
    private void initButtonTooltips()
    {
        Tooltip ttSearchPrevious        = new Tooltip("Previous Instance");
        Tooltip ttSearchNext            = new Tooltip("Next Instance");
        
        Font ttExplorerStandardFont     = new Font("System", 12);
        
        ttSearchPrevious.setFont(ttExplorerStandardFont);
        ttSearchNext.setFont(ttExplorerStandardFont);
        
        btnSearchPrevious.setTooltip(ttSearchPrevious);
        btnSearchNext.setTooltip(ttSearchNext);
        btnSearchPreviousFullScreen.setTooltip(ttSearchPrevious);
        btnSearchNextFullScreen.setTooltip(ttSearchNext);
    }
    private void initMouseEventHandlers()
    {
        final EventHandler<MouseEvent>  ehMouseClick_btnSearchPrevious = (MouseEvent me) -> {
            setSearchHighlight(SEARCH_HIGHLIGHT_PREVIOUS);
        };
        final EventHandler<MouseEvent>  ehMouseClick_btnSearchNext = (MouseEvent me) -> {
            setSearchHighlight(SEARCH_HIGHLIGHT_NEXT);
        };
        
        btnSearchPrevious.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnSearchPrevious);
        btnSearchNext.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnSearchNext);
        btnSearchPreviousFullScreen.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnSearchPrevious);
        btnSearchNextFullScreen.addEventHandler(MouseEvent.MOUSE_CLICKED, ehMouseClick_btnSearchNext);
    }
    private void initKeyEventHandlers()
    {
        final EventHandler<KeyEvent>  ehKeyPressed_taText = (KeyEvent ke) -> {
            
            switch(ke.getCode())
            {
                case TAB:
                {
                    if(ke.isShiftDown())        btnTabTextBackward();
                    else                        btnTabTextForward();
                        
                    ke.consume();
                    
                    break;
                }
                case ENTER:
                {
                    if(!isCaretWithinWhitespaceAtStartOfLine())
                    {
                        setAutoIndentationAmount();

                        autoIndentation();

                        ke.consume();  
                    }
                    
                    break;
                }
            }
        };
        
        taCode.addEventHandler(KeyEvent.KEY_PRESSED, ehKeyPressed_taText);
    }                   
    private void initKeyEventHandlersFullScreen()
    {
        final EventHandler<KeyEvent>  ehKeyPressed_apFullScreen = (KeyEvent ke) -> {
            
            if(ke.isControlDown())
            {
                switch(ke.getCode())
                {
                    case F:             setFullScreen(false, false);                        break;
                    case S:
                    {
                        if(objLibrary.isSelectedStandard())
                            objLibrary.save(SAVE_SELECTED);

                        break;
                    }
                }
                
            }
            else
            {
                switch(ke.getCode())
                {
                    case UP:            setSearchHighlight(SEARCH_HIGHLIGHT_PREVIOUS);      break;
                    case DOWN:          setSearchHighlight(SEARCH_HIGHLIGHT_NEXT);          break;
                    case ESCAPE:        setFullScreen(false,  false);                       break;
                }
            }
        };
        
        apFullScreen.addEventHandler(KeyEvent.KEY_PRESSED, ehKeyPressed_apFullScreen);
    }
    private void initEventListeners()
    {
        tpViewer.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                switch(tpViewer.getSelectionModel().getSelectedIndex())
                {
                    case 1:     refreshAsciiTable();            break;
                    case 2:     refreshEscapeSequences();       break;
                    case 3:     refreshInstructions();          break;
                }
            }
        });
        taCode.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                bCodeChanged = true;
            }
        });
        taFullScreen.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                bCodeChanged = true;
            }
        });
        tfHeader.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                bCodeChanged = true;
            }
        });
        labSearchNumberOfMatches.textProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue o, Object oldVal, Object newVal)
            {
                labSearchNumberOfMatchesFullScreen.setText(labSearchNumberOfMatches.getText());
            }
        });
    }
}