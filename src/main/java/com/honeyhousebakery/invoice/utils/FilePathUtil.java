package com.honeyhousebakery.invoice.utils;

import com.honeyhousebakery.invoice.domain.Client;
import com.honeyhousebakery.invoice.exceptions.DirectoryNotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.image4j.codec.ico.ICODecoder;
import net.sf.image4j.codec.ico.ICOEncoder;
import org.ini4j.Ini;

/**
 *
 * @author danielwind
 */
public class FilePathUtil {
    
    private static final String DESKTOP_PATH = System.getProperty("user.home") + File.separator + "Desktop";
    private static final String DEFAULT_MAIN_DIR_NAME = File.separator + "invoices";
    private static final String DEFAULT_MAIN_DIR_PATH = DESKTOP_PATH + DEFAULT_MAIN_DIR_NAME;
    private static final String RETAIL_DIR_NAME = "Retail";
    private static List<Client> DEFAULT_CLIENT_LIST = null;
    private static final String WINDOWS_DESKTOP_INI = File.separator + "desktop.ini";
    private static final String WINDOWS_FOLDER_ICON = File.separator + "Folder.ico";
    private static final String HOST_OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase();
    
    /**
     * This method creates the whole tree structure for the invoices directory.
     * It will be located at the Desktop (default) to ease access.
     */
    public static void createInvoicesMainDirectoryTreeStructure(List<Client> defaultClients) {
        
        System.out.println("-- DESKTOP DIRECTORY: " + DEFAULT_MAIN_DIR_PATH);
        
        DEFAULT_CLIENT_LIST = defaultClients;
        
        File file = new File(DEFAULT_MAIN_DIR_PATH);
        
        if(!file.exists()){
            
            Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "Main Directory not found, creating it...");
            
            if(file.mkdir()){
                Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "Main Directory created successfully!");
            } else {
                Logger.getLogger(FilePathUtil.class.getName()).log(Level.SEVERE, "Failed to create main directory");
                throw new DirectoryNotFoundException("Failed to create Main Directory, Please confirm you have writing permissions on this machine.");
            }
            
        } else {
            Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "Main Directory was found, checking tree existence...");
        }
        
        for(Client client : DEFAULT_CLIENT_LIST){
                
                File clientDir = new File(DEFAULT_MAIN_DIR_PATH + File.separator + client.getName());
                
                if(!clientDir.exists()){
                    
                    if(clientDir.mkdir()) {
                       Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "Client Directory ${client.getName()} created successfully!"); 
                    } else {
                       Logger.getLogger(FilePathUtil.class.getName()).log(Level.SEVERE, "Failed to create ${client.getName()} client directory"); 
                       throw new DirectoryNotFoundException("Failed to create client Directory, Please confirm you have writing permissions on this machine.");
                    }
                    
                } else {
                     Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "Client Directory ${client.getName()} already exists."); 
                }
         }
            
        //need to create a directory for custom clients
        File customClientDir = new File(DEFAULT_MAIN_DIR_PATH + File.separator + RETAIL_DIR_NAME);

        if(!customClientDir.exists()){

            if(customClientDir.mkdir()) {
                   Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "CUSTOM Client Directory created successfully!"); 
            } else {
                   Logger.getLogger(FilePathUtil.class.getName()).log(Level.SEVERE, "Failed to create custom client directory"); 
                   throw new DirectoryNotFoundException("Failed to create CUSTOM client Directory, Please confirm you have writing permissions on this machine.");
            }

        } else {
            Logger.getLogger(FilePathUtil.class.getName()).log(Level.INFO, "CUSTOM Client Directory already exists."); 

        }

        //SET FOLDER ICON
        if(HOST_OPERATING_SYSTEM.indexOf("win") >= 0){
            setWindowsFolderIconToMainDir();
        }else if(HOST_OPERATING_SYSTEM.indexOf("nix") >=0 || 
                 HOST_OPERATING_SYSTEM.indexOf("nux") >=0 || 
                 HOST_OPERATING_SYSTEM.indexOf("mac") >=0){
            setUnixFolderIconToMainDir();
        }
        
    }
    
    public static void setUnixFolderIconToMainDir() {
        try {
         
            String iconPath = DEFAULT_MAIN_DIR_PATH + WINDOWS_FOLDER_ICON;
            
            InputStream stream = FilePathUtil.class.getClassLoader().getResourceAsStream("Folder.ico");
            
            List<BufferedImage> iconFiles = ICODecoder.read(stream);
            
            ICOEncoder.write(iconFiles, new File(iconPath));
        
        } catch (IOException ex) {
            Logger.getLogger(FilePathUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setWindowsFolderIconToMainDir() {
        try {
            
            //store image in main dir
            String iconPath = DEFAULT_MAIN_DIR_PATH + WINDOWS_FOLDER_ICON;
            
            InputStream stream = FilePathUtil.class.getClassLoader().getResourceAsStream("Folder.ico");
            
            List<BufferedImage> iconFiles = ICODecoder.read(stream);
            
            ICOEncoder.write(iconFiles, new File(iconPath));
            
            //locate Desktop.ini
            File desktopIniFile = new File(DEFAULT_MAIN_DIR_PATH + WINDOWS_DESKTOP_INI);
            
            if(!desktopIniFile.exists()){
                 desktopIniFile.createNewFile();
            }
            
            Ini ini = new Ini(desktopIniFile);
            ini.put(".ShellClassInfo", "IconResource", iconPath + ",0");
            ini.store();
        
        } catch (IOException ex) {
            Logger.getLogger(FilePathUtil.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    /**
     * This method returns the invoices directory path
     * @return String main directory path
     */
    public static String getInvoicesMainDirectoryPath() {
        return DEFAULT_MAIN_DIR_PATH + File.separator;
    }
    
    
    /**
     * This method returns the client subdirectory path. In case it does not
     * exist, then it will be created and its full path returned.
     * @param clientName String the client name
     * @return String client subdirectory path
     */
    public static String getClientSubdirectoryPath(String clientName) {
        
        String clientSubDirectoryPath = DEFAULT_MAIN_DIR_PATH;
        
        if(clientName.equals(RETAIL_DIR_NAME)){
            clientSubDirectoryPath += (File.separator + RETAIL_DIR_NAME + File.separator);
        
        } else {
            
            for (Client client : DEFAULT_CLIENT_LIST) {
               
                if(client.getName().equals(clientName)){
                    clientSubDirectoryPath += (File.separator + client.getName() + File.separator);
                    break;
                }
            }
        }
        
        return clientSubDirectoryPath;
    }
    
}
