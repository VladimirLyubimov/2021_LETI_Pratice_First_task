package FileIO;

import Graph.MyGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class FromFile {
    private static Logger logger = LogManager.getLogger(FileIO.FromFile.class);

    public static Optional<MyGraph> read(String filename){//читаем информацию о графе из файла
        StringBuilder st = new StringBuilder("");

        try(FileReader reader = new FileReader(filename))
        {
            int symbol;
            while((symbol = reader.read()) != -1){
                st.append((char)symbol);
            }
        }
        catch(IOException err){
            logger.error(err.getMessage(), err);
            return Optional.ofNullable(null);
        }

        MyGraph graph;
        graph = Converter.fromJson(new String(st));

        return Optional.ofNullable(graph);
    }
}
