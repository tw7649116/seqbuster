/*

 */

package miraligner;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.beust.jcommander.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;

public class Main {


    public static void main(String[] args) throws FileNotFoundException, IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
        logger.addHandler(new ConsoleHandler());
        String format = "None";
        String test="test";
        if (test.equals("notest")){
            LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.ALL);
            for (Handler h : logger.getHandlers()){
                h.setLevel(Level.ALL);
            }
            logger.config("Entering in test mode.");
            map.readseq("test/test.fa","DB","hsa",1,5,5,"fasta","test/test",true,true,16);
            System.exit(0);
        }
        Options jct = new Options();
        JCommander jc = new JCommander(jct, args);
        if (jct.help | args.length<4 ){
            jc.usage();
            System.out.println("\njava -jar miraligner.jar -minl 16 -sub mismatches -trim trimming-nts -add addition-nts -s species -i read_seq_file -db miRBase_folder_files -o output_file");
            System.out.println("\nexample:java -jar miraligner.jar -sub 1 -trim 3 -add 3 -s hsa -i test/test.fa -db DB -o test/out");
            System.out.println("example: see output at miraligner/test/output.mirna & miraligner/test/output.mirna.opt");
            System.out.println("\n");
            System.exit(0);
        }
        if (jct.version) {
            System.out.println("version 2");
            System.exit(0);
        }
        //check input file
        if ("none".equals(jct.format)){
            boolean f=tools.checkinput(jct.input);
            boolean ftab=tools.checkinputtab(jct.input);
            if (f){
                format="fasta";
            }else if (ftab){
                format="tab";
            }else{
                System.err.println("no format file recognized (fasta or tabular)");
                System.exit(1);
            }
        }else{
            format = jct.format;
        }
        
        if (jct.debug){
            LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.ALL);
            for (Handler h : logger.getHandlers()){
                h.setLevel(Level.ALL);
            }
            logger.finest("Entering in debug mode.");
        }
        //check species
        boolean sp=tools.checksp(jct.db,jct.species);
        int mism=Integer.parseInt(jct.sub);
        if (mism>1){
           System.out.println("Only allowed 0/1 mismatch");
           sp=false;
        }
        int trim=Integer.parseInt(jct.trim);
        if (trim>5){
           System.out.println("Only allowed <=5 nucleotides as trimming");
           sp=false;
        }
        int add=Integer.parseInt(jct.add);
        if (add>5){
           System.out.println("Only allowed <=5 nucleotides as addition");
           sp=false;
        }
        if (jct.minl<16){
           System.out.println("Only allowed >=16 minimum size");
           jct.minl=16;
        }
        if (sp  ){
            System.out.println("Go to mapping...");
            System.out.println("Mismatches: "+jct.sub);
            System.out.println("Trimming: "+jct.trim);
            System.out.println("Addition: "+jct.add);
            System.out.println("Species: "+jct.species);
            
            map.readseq(jct.input,jct.db,jct.species,mism,trim,add,format,jct.output,jct.freq,jct.pre,jct.minl);

       }else{
           System.err.println("species not found: "+jct.species);
           System.exit(1); 
        }
    }



}
