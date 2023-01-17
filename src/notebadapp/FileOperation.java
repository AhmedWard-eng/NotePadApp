/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notebadapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author AhmedWard
 */
public class FileOperation {
    private String recentDirectory = "E:\\ITI\\java\\notebadFiles";
    FileChooser fileChooser;

    public FileOperation() {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(recentDirectory));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"), new FileChooser.ExtensionFilter("All Files", "*.*"));
    }
    
    
    
    public boolean saveFile(Stage stage,byte[] bytes){
        File file = fileChooser.showSaveDialog(stage);
        FileOutputStream fos;
        if (file != null) {
            setIntialDirectory(file.getParent());
            try {
                fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true; 
        }else{
            return false;
        }
    }
    
    
    public String getFileData(Stage stage){
        File file =  fileChooser.showOpenDialog(stage);
        FileInputStream fis;
        String fileData = "";
        if (file != null) {
            setIntialDirectory(file.getParent());
            try {
                fis = new FileInputStream(file);
                int size = fis.available();
                byte[] b = new byte[size];
                fis.read(b);
                fileData = new String(b);
                fis.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentBase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return fileData;
    }
    private void setIntialDirectory(String recentDirectory){
        fileChooser.setInitialDirectory(new File(recentDirectory));
    }
}
