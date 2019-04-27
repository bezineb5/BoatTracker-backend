package courageous.webservices.resources;

import java.util.UUID;

import courageous.webservices.exceptions.InvalidRequestException;

public class ResourcesHelper {
    private ResourcesHelper() {
    }

    static UUID parseUuid(String strUuid) throws InvalidRequestException {
        if (isEmpty(strUuid)) {
            throw new InvalidRequestException("Missing identifier");
        }

        try {
            return UUID.fromString(strUuid);
        } catch(IllegalArgumentException e) {
            throw new InvalidRequestException("Wrong identifier format");
        }
    }

    static boolean isEmpty(String str) {
        return (str == null || "".equals(str));
    }
}
