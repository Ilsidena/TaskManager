package classes.model;
import java.util.Objects;

/**
 * @author anna
 */

public class Note {
    private String name;
    private String description;

    public Note (String name
                ,String description) {
        this.name = name;
        this.description = description;
    }

    public Note(){ }

    // Setters
    
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    // Getters
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Note other = (Note) obj;
        
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        
        return true;
    }
    
    public String toString (){
        return new String (name + "\n" + description);
    }
}