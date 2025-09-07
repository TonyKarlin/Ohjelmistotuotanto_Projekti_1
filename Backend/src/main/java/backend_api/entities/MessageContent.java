package backend_api.entities;


import jakarta.persistence.*;

@Entity
public class MessageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    private String fileType;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    public MessageContent() {
    }

    public MessageContent(byte[] data, String fileType, Message message) {
        this.data = data;
        this.fileType = fileType;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
