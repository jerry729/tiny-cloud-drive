package filesystem.document;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

/**
 * @author :frank
 * @date :8:44 2020/11/26
 * @description :TODO
 */
public class Doc implements Serializable {
    private int Id;
    private String creator;
    private Timestamp timestamp;
    private String description;
    private String filename;

    public Doc(){}

    public Doc(int id, String creator,Timestamp timestamp, String description, String filename){

        super();
        this.Id = id;
        this.creator = creator;
        this.timestamp = timestamp;
        this.description = description;
        this.filename = filename;
        Path p = Paths.get("D:","Documents","upload");
        Path p1 = Paths.get("D:","Documents","upload",id + ".txt");
    }



    public int getId(){
        return Id;
    }

    public void setID(int id){
        this.Id = id;
    }

    public String getCreator(){
        return creator;
    }

    public void setCreator(String creator){
        this.creator = creator;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getFilename(){
        return filename;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "["+ Id + "," + creator + "," + timestamp + "," + "," + description + "," + filename + "]";
    }
}
