package BaseClass;

import java.io.Serializable;
import java.time.LocalDate;

public class Human implements Serializable {
    private static final long serialVersionUID = 32L;

    private LocalDate birthday;

    public Human(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}
