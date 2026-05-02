package com.pfelink.monolith.shared.util;

import com.pfelink.monolith.domain.academic.entity.faculty.Address;

public class AddressParser {

    public static Address parse(String inputPath) {
        // Sanitize path for ltree: replace spaces/commas with underscores and remove other invalid chars
        String sanitizedPath = inputPath.trim()
            .replaceAll("[ ,]+", "_")
            .replaceAll("[^a-zA-Z0-9_.]", "");
            
        Address address = new Address();
        address.setPath(sanitizedPath);
        
        String[] parts = sanitizedPath.split("\\.");
        int componentIndex = 0;

        for (String part : parts) {
            String decoded = part.replace("_", " ");
            
            if (part.startsWith("lat_")) {
                try {
                    address.setLatitude(Double.parseDouble(part.substring(4).replace("_", ".")));
                } catch (NumberFormatException ignored) {}
            } else if (part.startsWith("lon_")) {
                try {
                    address.setLongitude(Double.parseDouble(part.substring(4).replace("_", ".")));
                } catch (NumberFormatException ignored) {}
            } else if (part.startsWith("pc_")) {
                address.setPostalCode(decoded.substring(3));
            } else {
                // Hierarchical mapping
                switch (componentIndex) {
                    case 0 -> address.setCountry(decoded);
                    case 1 -> address.setRegion(decoded);
                    case 2 -> address.setCity(decoded);
                    case 3 -> address.setSuburb(decoded);
                    case 4 -> address.setStreet(decoded);
                    case 5 -> address.setHouseNumber(decoded);
                }
                componentIndex++;
            }
        }
        
        // Build a display name if missing
        if (address.getDisplayName() == null) {
            address.setDisplayName(sanitizedPath.replace(".", ", ").replace("_", " "));
        }
        
        return address;
    }
}
