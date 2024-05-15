/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SampleCode;

import SampleCode.library.Library;
import SampleCode.library.Node;
import SampleCode.shared.*;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author PaulSu
 */
public class SampleCode implements Initializable, Constants {

    @FXML
    private SplitPane               spGlobal;

        //SplitPane 1 - Library
        @FXML
        private TreeView<Node>          tvLibrary;
        @FXML
        private Button                  btnSearchByName;
        @FXML
        private Button                  btnSearchByCode;
        @FXML
        private TextField               tfSearch;

        //SplitPane 2 - Viewer
        @FXML
        private TabPane                 tpViewer;

            //Tab - Code
            @FXML
            private TextField               tfHeader;
            @FXML
            private ToolBar                 tbBreadcrumbTrail;
            @FXML
            private Label                   labSearchNumberOfMatches;
            @FXML
            private ImageView               ivSaveIcon;
            @FXML
            private TextArea                taCode;
            @FXML
            private Button                  btnSearchPrevious;
            @FXML
            private Button                  btnSearchNext;

            //Tab - Ascii Table
            @FXML
            private TextArea                taAsciiTable;
            
            //Tab - Escape Sequences
            @FXML
            private TextArea                taEscapeSequences;
            
            //Tab - Shortcut Keys
            @FXML
            private TextArea                taInstructions;
    
    @FXML
    private AnchorPane              apFullScreen;
    @FXML
    private TextArea                taFullScreen;
    
            @FXML
            private Button                  btnSearchPreviousFullScreen;
            @FXML
            private Button                  btnSearchNextFullScreen;
            @FXML
            private Label                   labSearchNumberOfMatchesFullScreen;
            @FXML
            private ImageView               ivSaveIconFullScreen;
            @FXML
            private ImageView               ivSaveIconFullScreenWithSearch;
    
    @FXML
    private AnchorPane              apDeleteConfirmation;
    
            @FXML
            private Button                  btnDeleteConfirm;
            @FXML
            private Button                  btnDeleteCancel;
    
    @FXML
    private AnchorPane              apContentExceedMaximum;
    
            @FXML
            private Label                   labContentExceedMaximum;
            @FXML
            private Button                  btnContentExceedMaximumClose;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Database.initDatabaseConnection();
      
        Library objLibrary = new Library(spGlobal,
                                            tvLibrary,              tfSearch,                                           //Library
                                            btnSearchByName,        btnSearchByCode,                                    //Library
                                            tpViewer,
                                                tfHeader,           tbBreadcrumbTrail,      ivSaveIcon,    taCode,      //Viewer - Tab - Code
                                                btnSearchPrevious,  btnSearchNext,          labSearchNumberOfMatches,   //Viewer - Tab - Code
                                                taAsciiTable,                                                           //Viewer - Tab - Ascii Table
                                                taEscapeSequences,                                                      //Viewer - Tab - Escape Sequences
                                                taInstructions,                                                         //Viewer - Tab - Instructions
                                            apFullScreen, taFullScreen,                                                 //Full Screen
                                                btnSearchPreviousFullScreen, btnSearchNextFullScreen,                   //Full Screen
                                                labSearchNumberOfMatchesFullScreen,                                     //Full Screen
                                                ivSaveIconFullScreen, ivSaveIconFullScreenWithSearch,                   //Full Screen
                                            apDeleteConfirmation, btnDeleteConfirm, btnDeleteCancel,                    //Delete Confirmation
                                            apContentExceedMaximum, labContentExceedMaximum,                            //Content Exceed Maximum
                                            btnContentExceedMaximumClose);                                              //Content Exceed Maximum
    }
}