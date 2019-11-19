package hotelicus.enums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public enum UserPrivileges {
    ADMIN,
    OWNER,
    MANAGER,
    RECEPTIONIST
}
