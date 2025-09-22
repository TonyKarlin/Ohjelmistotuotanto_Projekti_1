package backend_api.entities;

import backend_api.enums.ContactStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contacts")
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "contact_user_id", nullable = false)
    private User contact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Column for tracking updates. E.g. when status changes.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    public Contacts() {
    }

    // getterit ja setterit
    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUserId() {
        return user;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public User getContact() {
        return contact;
    }

    public void setContactId(User contact) {
        this.contact = contact;
    }

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

}
