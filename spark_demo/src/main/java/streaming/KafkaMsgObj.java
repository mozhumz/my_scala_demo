package streaming;

import lombok.Data;

import java.io.Serializable;

@Data
public class KafkaMsgObj implements Serializable {
    private String name;
    private String id;

}
