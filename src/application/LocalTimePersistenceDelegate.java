package application;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.time.LocalTime;

// Override på två metoder från klassen "PersistenceDelegate" från java.beans, som används av XMLEncoder och XMLDecoder för att hantera specialfall vid serialisering och deserialisering av objekt.
// Löser alltså i detta fall hanteringen av datatypen LocalTime.
public class LocalTimePersistenceDelegate extends PersistenceDelegate {

    @Override
    protected Expression instantiate(Object oldInstance, Encoder out) {
        LocalTime time = (LocalTime) oldInstance;
        return new Expression(oldInstance, oldInstance.getClass(), "parse", new Object[]{time.toString()});
    }

    @Override
    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        return oldInstance.equals(newInstance);
    }
}
