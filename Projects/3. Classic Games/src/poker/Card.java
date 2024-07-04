package poker;

import app.App;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

class Card implements Constants{

    private CardManager objCardManager;
    private AnchorPane  apPokerTable;
    
    private ImageView   ivSprite;
    private Rectangle   recPlayerCardCover;
    private Label       labPlayerCardCoverText;
        
    private String      strSuit;
    
    private int iSuit, iValue, iSuitStore, iValueStore, iPlayerNumber, iTablePosition, iHoldState, iFoldState;
    private boolean bInSet, bCardSelectable;
    
    Card(CardManager objCardManager, AnchorPane apPokerTable, int iPlayerNumber, int iCardNumber) {
        
        this.objCardManager             = objCardManager;
        this.apPokerTable               = apPokerTable;
        this.iPlayerNumber              = iPlayerNumber;
        this.iTablePosition             = iCardNumber;
        
        //Create the card, the card cover and card text and add to the Anchor Pane
        createCard();
        
        //Initialised to card back
        iSuit           = BACK;
        iValue          = BACK;
        iHoldState      = CARD_HOLD_OFF;
        iFoldState      = CARD_FOLD_OFF;
        bInSet          = false;
        bCardSelectable = true;
    }
    
    
    
    private void createCard()
    {
        //Create the Card sprite, cover and text depending on the player number/position around teh table...
        if( (iPlayerNumber == PLAYER_1) || (iPlayerNumber == PLAYER_3) )
        {
            ivSprite = new ImageView(App.class.getResource("images/poker/Card Back - Vertical.png").toString());
            ivSprite.setOpacity(NOT_VISIBLE);
            ivSprite.setLayoutX(objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, X_LAYOUT_COORDINATE));
            ivSprite.setLayoutY(objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, Y_LAYOUT_COORDINATE));
            
            //Rectangle(double x, double y, double width, double height)
            recPlayerCardCover              = new Rectangle(objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, X_LAYOUT_COORDINATE),
                                                            objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, Y_LAYOUT_COORDINATE),
                                                            (double)CARD_COVER_WIDTH_VERTICAL, (double)CARD_COVER_HEIGHT_VERTICAL);
            recPlayerCardCover.setFill                  (Paint.valueOf("#000000"));
            recPlayerCardCover.setOpacity               (NOT_VISIBLE);
            
            
            labPlayerCardCoverText          = new Label();
            labPlayerCardCoverText.setLayoutX           (objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, X_LAYOUT_COORDINATE));
            labPlayerCardCoverText.setLayoutY           (objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, Y_LAYOUT_COORDINATE)); 
            labPlayerCardCoverText.setPrefSize          (CARD_TEXT_WIDTH_VERTICAL, CARD_TEXT_HEIGHT_VERTICAL);
            labPlayerCardCoverText.setFont              (Font.font("System", FontWeight.BOLD, 24));
            labPlayerCardCoverText.setTextAlignment     (TextAlignment.CENTER);
            labPlayerCardCoverText.setAlignment         (Pos.CENTER);
            labPlayerCardCoverText.setTextFill          (Paint.valueOf("RED"));
            
            labPlayerCardCoverText.setOpacity           (NOT_VISIBLE);
            labPlayerCardCoverText.setText              ("");
        }
        else
        {
            ivSprite                        = new ImageView(App.class.getResource("images/poker/Card Back - Horizontal.png").toString());
            ivSprite.setOpacity(NOT_VISIBLE);
            ivSprite.setLayoutX(objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, X_LAYOUT_COORDINATE));
            ivSprite.setLayoutY(objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, Y_LAYOUT_COORDINATE));
            
            //Rectangle(double x, double y, double width, double height)
            recPlayerCardCover              = new Rectangle(objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, X_LAYOUT_COORDINATE),
                                                            objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, Y_LAYOUT_COORDINATE),
                                                            (double)CARD_COVER_WIDTH_HORIZONTAL, (double)CARD_COVER_HEIGHT_HORIZONTAL);
            recPlayerCardCover.setFill                  (Paint.valueOf("#000000"));
            recPlayerCardCover.setOpacity               (NOT_VISIBLE);
            
            labPlayerCardCoverText          = new Label();
            labPlayerCardCoverText.setLayoutX           (objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, X_LAYOUT_COORDINATE));
            labPlayerCardCoverText.setLayoutY           (objCardManager.getCardLayoutCoordinates(iPlayerNumber, iTablePosition, Y_LAYOUT_COORDINATE));    
            labPlayerCardCoverText.setPrefSize          (CARD_TEXT_WIDTH_HORIZONTAL, CARD_TEXT_HEIGHT_HORIZONTAL);
            labPlayerCardCoverText.setFont              (Font.font("System", FontWeight.BOLD, 24));
            labPlayerCardCoverText.setTextAlignment     (TextAlignment.CENTER);
            labPlayerCardCoverText.setAlignment         (Pos.CENTER);
            labPlayerCardCoverText.setTextFill          (Paint.valueOf("RED"));
            
            labPlayerCardCoverText.setOpacity           (NOT_VISIBLE);
            labPlayerCardCoverText.setText              ("");
        }
        
        apPokerTable.getChildren().add(ivSprite);
        apPokerTable.getChildren().add(recPlayerCardCover);
        apPokerTable.getChildren().add(labPlayerCardCoverText);
    }
    private void showFold()
    {
        recPlayerCardCover.setOpacity(CARD_HIGHLIGHT_OPACITY_ON);
        labPlayerCardCoverText.setOpacity(VISIBLE);
        labPlayerCardCoverText.setText("Fold");
    }
    private void removeFold()
    {
        recPlayerCardCover.setOpacity(CARD_HIGHLIGHT_OPACITY_OFF);
        labPlayerCardCoverText.setOpacity(CARD_HIGHLIGHT_OPACITY_OFF);
        labPlayerCardCoverText.setText("");
    }
    private void showHold()
    {
        recPlayerCardCover.setOpacity(CARD_HIGHLIGHT_OPACITY_ON);
        labPlayerCardCoverText.setOpacity(VISIBLE);
        labPlayerCardCoverText.setText("Hold");
    }
    private void removeHold()
    {
        recPlayerCardCover.setOpacity(CARD_HIGHLIGHT_OPACITY_OFF);
        labPlayerCardCoverText.setOpacity(CARD_HIGHLIGHT_OPACITY_OFF);
        labPlayerCardCoverText.setText("");
    }
    
    
    
    void setCardSelectable(boolean iState)
    {
        bCardSelectable = iState;
    }
    boolean getCardSelectable()
    {
        return bCardSelectable;
    }
    void setSuit(int iSuit)
    {
        this.iSuit = iSuit;
        
        setSuitStore(iSuit);
    }
    void setValue(int iValue)
    {
        this.iValue = iValue;
        
        setValueStore(iValue);
    }
    void setSuitStore(int iSuit)
    {
        iSuitStore = iSuit;
    }
    void setValueStore(int iValue)
    {
        iValueStore = iValue;
    }
    void setCardOpacity(int iOpacity)
    {
        ivSprite.setOpacity(iOpacity);
    }
    void setTablePosition(int iTablePosition)
    {
        this.iTablePosition = iTablePosition;
    }
    void setInSet(boolean bState)
    {
        bInSet = bState;
    }
    void toggleHoldState()
    {
        switch(iHoldState)
        {
            case(CARD_HOLD_ON):         setHoldState(CARD_HOLD_OFF);        break;
            case(CARD_HOLD_OFF):        setHoldState(CARD_HOLD_ON);         break;
        }
    }
    void setHoldState(int iState)
    {
        iHoldState = iState;
        
        switch(iHoldState)
        {
            case(CARD_HOLD_ON):         showHold();             break;
            case(CARD_HOLD_OFF):        removeHold();           break;
        }
    }
    void setFoldState(int iState)
    {
        iFoldState = iState;
        
        switch(iFoldState)
        {
            case(CARD_FOLD_ON):         showFold();             break;
            case(CARD_FOLD_OFF):        removeFold();           break;
        }
    }
    int getSuit()
    {
        return iSuit;
    }
    int getValue()
    {
        return iValue;
    }
    int getSuitStore()
    {
        return iSuitStore;
    }
    int getValueStore()
    {
        return iValueStore;
    }
    int getTablePosition()
    {
        return iTablePosition;
    }
    int getHold()
    {
        return iHoldState;
    }
    int getFold()
    {
        return iFoldState;
    }
    boolean getInSet()
    {
        return bInSet;
    }
    DoubleProperty getCardOpacityProperty()
    {
        return ivSprite.opacityProperty();
    }
    Rectangle getRecCardCoverRef()
    {
        return recPlayerCardCover;
    }
    Label getLabPlayerCardCoverTextRef()
    {
        return labPlayerCardCoverText;
    }
    void showCardCover()
    {
        recPlayerCardCover.setOpacity(CARD_HIGHLIGHT_OPACITY_ON);
    }
    void hideCardCover()
    {
        recPlayerCardCover.setOpacity(CARD_HIGHLIGHT_OPACITY_OFF);
    }
    void clearCardCoverText()
    {
        labPlayerCardCoverText.setText("");
    }
    void loadCard()
    {
        switch(iSuitStore)
        {
            case(CLUBS):            strSuit = "Clubs";          break;
            case(HEARTS):           strSuit = "Hearts";         break;
            case(SPADES):           strSuit = "Spades";         break;
            case(DIAMONDS):         strSuit = "Diamonds";       break;
        }
      
        //Evaluate and show the corresponding card image...
        if( (iPlayerNumber == PLAYER_1) || (iPlayerNumber == PLAYER_3) )     //Show vertical cards
        {
            if( (iValueStore >= 2) && (iValueStore <= 10) )
            {
                ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Vertical/" + Integer.toString(iValueStore) + " - " + strSuit + ".PNG").toString()));
            }
            else
            {
                switch(iValue)
                {
                    case(JACK):     ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Vertical/Jack - " + strSuit + ".PNG").toString()));         break;
                    case(QUEEN):    ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Vertical/Queen - " + strSuit + ".PNG").toString()));        break;
                    case(KING):     ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Vertical/King - " + strSuit + ".PNG").toString()));         break;
                    case(ACE):      ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Vertical/Ace - " + strSuit + ".PNG").toString()));          break;
                    case(BACK):
                    {
                        ivSprite.setImage(new Image(App.class.getResource("images/poker/Card Back - Vertical.png").toString()));
                        
                        setFoldState(CARD_FOLD_OFF);
        
                        break;
                    }
                }
            }
        }
        else if( (iPlayerNumber == PLAYER_2) || (iPlayerNumber == PLAYER_4) )     //Show horizontal cards
        {   
            if( (iValueStore >= 2) && (iValueStore <= 10) )
            {
                ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Horizontal/" + Integer.toString(iValueStore) + " - " + strSuit + " - Horizontal.PNG").toString()));
            }
            else
            {
                switch(iValue)
                {
                    case(JACK):     ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Horizontal/Jack - " + strSuit + " - Horizontal.PNG").toString()));          break;
                    case(QUEEN):    ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Horizontal/Queen - " + strSuit + " - Horizontal.PNG").toString()));         break;
                    case(KING):     ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Horizontal/King - " + strSuit + " - Horizontal.PNG").toString()));          break;
                    case(ACE):      ivSprite.setImage(new Image(App.class.getResource("images/poker/" + strSuit + "/Horizontal/Ace - " + strSuit + " - Horizontal.PNG").toString()));           break;
                    case(BACK):
                    {
                        ivSprite.setImage(new Image(App.class.getResource("images/poker/Card Back - Horizontal.png").toString()));
                        
                        setFoldState(CARD_FOLD_OFF);
        
                        break;
                    }
                }
            }
        }
    }
    void hide()
    {
        ivSprite.setOpacity(0);
    }
}
