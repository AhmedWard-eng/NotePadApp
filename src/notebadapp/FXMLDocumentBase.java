package notebadapp;

import com.sun.javafx.charts.ChartLayoutAnimator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class FXMLDocumentBase extends BorderPane {

    protected final MenuBar menuBar;
    protected final Menu menuFile;
    protected final MenuItem menuItemNew;
    protected final MenuItem menuItemOpen;
    protected final MenuItem menuItemSave;
    protected final MenuItem menuItemExit;
    protected final Menu menuEdit;
    protected final MenuItem menuItemCut;
    protected final MenuItem menuItemCopy;
    protected final MenuItem menuItemPaste;
    protected final MenuItem menuItemDelete;
    protected final MenuItem menuItemSelectAll;
    protected final Menu menu;
    protected final MenuItem MenuItemAbout;
    protected final TextArea textArea;
    private String copiedText;
    private Alert confirmationAlert;
    private FileOperation fileOperation;

    public FXMLDocumentBase(Stage stage) {

        menuBar = new MenuBar();
        menuFile = new Menu();
        menuItemNew = new MenuItem();
        menuItemOpen = new MenuItem();
        menuItemSave = new MenuItem();
        menuItemExit = new MenuItem();
        menuEdit = new Menu();
        menuItemCut = new MenuItem();
        menuItemCopy = new MenuItem();
        menuItemPaste = new MenuItem();
        menuItemDelete = new MenuItem();
        menuItemSelectAll = new MenuItem();
        menu = new Menu();
        MenuItemAbout = new MenuItem();
        textArea = new TextArea();
        fileOperation = new FileOperation();

        confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Notepad");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Do you want to save changes to Untitled");

        ButtonType buttonTypeOne = new ButtonType("save");
        ButtonType buttonDontSave = new ButtonType("Don't save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        confirmationAlert.getButtonTypes().setAll(buttonTypeOne, buttonDontSave, buttonTypeCancel);

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        BorderPane.setAlignment(menuBar, javafx.geometry.Pos.CENTER);

        menuFile.setId("menuFile");
        menuFile.setMnemonicParsing(false);
        menuFile.setText("File");

        menuItemNew.setId("menuItemNew");
        menuItemNew.setMnemonicParsing(false);
        menuItemNew.setText("New");
        menuItemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));

        menuItemOpen.setId("menuItemOpen");
        menuItemOpen.setMnemonicParsing(false);
        menuItemOpen.setText("Open");
        menuItemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

        menuItemSave.setId("menuItemSave");
        menuItemSave.setMnemonicParsing(false);
        menuItemSave.setText("Save");
        menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        menuItemExit.setId("menuItemExit");
        menuItemExit.setMnemonicParsing(false);
        menuItemExit.setText("Exit");
        menuItemExit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        menuEdit.setId("menuEdit");
        menuEdit.setMnemonicParsing(false);
        menuEdit.setText("Edit");

        menuItemCut.setId("menuItemCut");
        menuItemCut.setMnemonicParsing(false);
        menuItemCut.setText("Cut");
        menuItemCut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

        menuItemCopy.setId("menuItemCopy");
        menuItemCopy.setMnemonicParsing(false);
        menuItemCopy.setText("Copy");
        menuItemCopy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        menuItemPaste.setId("menuItemPaste");
        menuItemPaste.setMnemonicParsing(false);
        menuItemPaste.setText("Paste");
        menuItemPaste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));

        menuItemDelete.setId("menuItemDelete");
        menuItemDelete.setMnemonicParsing(false);
        menuItemDelete.setText("Delete");

        menuItemSelectAll.setId("menuItemSelectAll");
        menuItemSelectAll.setMnemonicParsing(false);
        menuItemSelectAll.setText("Select All");
        menuItemSelectAll.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        menu.setId("menuHelp");
        menu.setMnemonicParsing(false);
        menu.setText("Help");

        MenuItemAbout.setId("MenuItemAbout");
        MenuItemAbout.setMnemonicParsing(false);
        MenuItemAbout.setText("About");
        setTop(menuBar);

        BorderPane.setAlignment(textArea, javafx.geometry.Pos.CENTER);
        textArea.setId("textArea");
        textArea.setPrefHeight(200.0);
        textArea.setPrefWidth(200.0);
        setCenter(textArea);

        menuItemSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                byte[] bytes = textArea.getText().getBytes();
                fileOperation.saveFile(stage, bytes);
            }
        });

        menuItemOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                byte[] outBytes = textArea.getText().getBytes();
                if (outBytes.length > 0) {
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.get() == buttonTypeOne) {

                        boolean isSaved = fileOperation.saveFile(stage, outBytes);
                        if (isSaved) {
                            String data = fileOperation.getFileData(stage);
                            textArea.setText(data);
                        }
                    } else if (result.get() == buttonDontSave) {
                        String data = fileOperation.getFileData(stage);
                        textArea.setText(data);

                    } else if (result.get() == buttonTypeCancel) {
                        // ... user chose "Three"
                    }

                } else {
                    String data = fileOperation.getFileData(stage);
                    textArea.setText(data);
                }
            }
        });
        menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                byte[] b = textArea.getText().getBytes();
                if (b.length > 0) {
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        boolean isSaved = fileOperation.saveFile(stage, b);
                        if (isSaved) {
                            textArea.clear();
                        }

                    } else if (result.get() == buttonDontSave) {
                        textArea.clear();
                    } else if (result.get() == buttonTypeCancel) {
                        // ... user chose "Three"
                    }
//                    System.out.println("hello new");

                } else {
                    textArea.clear();
                }

            }
        });

        menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                byte[] b = textArea.getText().getBytes();
                if (b.length > 0) {
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        boolean isSaved = fileOperation.saveFile(stage, b);
                        if (isSaved) {
                            System.exit(0);
                        }
                    } else if (result.get() == buttonDontSave) {
                        System.exit(0);
                    } else if (result.get() == buttonTypeCancel) {
                        // ... user chose "Three"
                    }

                } else {
                    System.exit(0);
                }
            }
        });

        menuItemCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!"".equals(textArea.getSelectedText())) {
                    copiedText = textArea.getSelectedText();
                }
            }
        });
        menuItemPaste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (copiedText != null) {
                    textArea.deleteText(textArea.getSelection());
                    textArea.appendText(copiedText);
                }
            }
        });

        menuItemCut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!"".equals(textArea.getSelectedText())) {
                    copiedText = textArea.getSelectedText();
                }
                textArea.deleteText(textArea.getSelection());
            }
        });

        menuItemDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.deleteText(textArea.getSelection());
            }
        });

        menuItemSelectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.selectAll();
            }
        });

        MenuItemAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("We are a good team from ITI and we made a notepad");
                alert.showAndWait();
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                byte[] b = textArea.getText().getBytes();
                if (b.length > 0) {
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.get() == buttonTypeOne) {
                        boolean isSaved = fileOperation.saveFile(stage, b);
                        if (!isSaved) {
                            event.consume();
                        }
                    } else if (result.get() == buttonDontSave) {

                        System.exit(0);
                    } else if (result.get() == buttonTypeCancel) {
                        event.consume();
                    }

                } else {
                }
            }
        });
        menuFile.getItems().add(menuItemNew);
        menuFile.getItems().add(menuItemOpen);
        menuFile.getItems().add(menuItemSave);
        menuFile.getItems().add(menuItemExit);
        menuBar.getMenus().add(menuFile);
        menuEdit.getItems().add(menuItemCut);
        menuEdit.getItems().add(menuItemCopy);
        menuEdit.getItems().add(menuItemPaste);
        menuEdit.getItems().add(menuItemDelete);
        menuEdit.getItems().add(menuItemSelectAll);
        menuBar.getMenus().add(menuEdit);
        menu.getItems().add(MenuItemAbout);
        menuBar.getMenus().add(menu);

    }

}
