package callback;

import java.io.IOException;
import java.util.List;

import model.Contact;

public interface ContactUpdateCallback {

    void onContactsUpdated(List<Contact> updatedContacts) throws IOException;
}
