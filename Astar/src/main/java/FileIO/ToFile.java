package FileIO;

import Graph.MyGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

public class ToFile {
    private static Logger logger = LogManager.getLogger(FileIO.ToFile.class);

    public static boolean write(MyGraph graph, String filename){//записываем граф в файл
        try(FileWriter writer = new FileWriter(filename, false))
        {
            writer.write(Converter.makeJson(graph));
            writer.flush();
        }
        catch(IOException err){
            logger.error(err.getMessage(), err);
            return false;
        }

        return true;
    }
}
