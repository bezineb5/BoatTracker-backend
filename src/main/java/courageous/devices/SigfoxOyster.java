package courageous.devices;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Consumer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class SigfoxOyster {
    public static final int MESSAGE_POSITIONAL_DATA = 0;
    public static final int MESSAGE_DOWNLINK_ACK = 1;
    public static final int MESSAGE_DEVICE_DATA = 2;
    public static final int MESSAGE_EXTENDED_POSITIONAL_DATA = 3;

    public static class Location {
        private final int messageType;
        private final boolean inTrip;
        private final boolean lastFixFailed;
        private final double latitude;
        private final double longitude;
        private final int heading;
        private final int speedKmH;
        private final double batteryVoltage;
    
        Location(int messageType, boolean inTrip, boolean lastFixFailed, double latitude, double longitude, int heading,
                int speedKmH, double batteryVoltage) {
            this.messageType = messageType;
            this.inTrip = inTrip;
            this.lastFixFailed = lastFixFailed;
            this.latitude = latitude;
            this.longitude = longitude;
            this.heading = heading;
            this.speedKmH = speedKmH;
            this.batteryVoltage = batteryVoltage;
        }
    
        public int getMessageType() {
            return this.messageType;
        }
    
        public boolean getInTrip() {
            return this.inTrip;
        }
    
        public boolean isInTrip() {
            return this.inTrip;
        }
    
        public boolean getLastFixFailed() {
            return this.lastFixFailed;
        }
    
        public boolean isLastFixFailed() {
            return this.lastFixFailed;
        }
    
        public double getLatitude() {
            return this.latitude;
        }
    
        public double getLongitude() {
            return this.longitude;
        }
    
        public int getHeading() {
            return this.heading;
        }
    
        public int getSpeedKmH() {
            return this.speedKmH;
        }
    
        public double getBatteryVoltage() {
            return this.batteryVoltage;
        }
    }

    public static Location parse(String hexData) {
        final byte[] payload;

        if (hexData == null || hexData.length() == 0) {
            return null;
        }

        try {
            payload = Hex.decodeHex(hexData.toCharArray());
        } catch (DecoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        if (payload == null || payload.length == 0) {
            return null;
        }

        final int messageType = payload[0] & 0x0f;
        final ByteBuffer buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN);

        switch (messageType) {
        case MESSAGE_POSITIONAL_DATA:
            return parsePositionalData(buffer);
        case MESSAGE_EXTENDED_POSITIONAL_DATA:
            return parseExtendedPositionalData(buffer);
        case MESSAGE_DOWNLINK_ACK:
            // Ignore for now
            break;
            //return parseDownlinkAck(buffer);
        case MESSAGE_DEVICE_DATA:
            // Ignore for now
            break;
            //return parseDeviceStats(buffer);
        default:
            break;
        }

        return null;
    }

    private static Location parsePositionalData(ByteBuffer buffer) {

        final int firstByte = buffer.get();
        final int messageType = firstByte & 0x0f;
        final boolean inTrip = (firstByte & 0x10) > 0;
        final boolean lastFixFailed = (firstByte & 0x20) > 0;

        final double latitude = (double) buffer.getInt() * 1e-7;
        final double longitude = (double) buffer.getInt() * 1e-7;
        final int heading = (int) buffer.get() * 2;
        final int speedKmH = buffer.get();
        final double batteryVoltage = (double) ((int) buffer.get() * 25) / 1000.0;

        final Location location = new Location(messageType, inTrip, lastFixFailed, latitude, longitude, heading,
                speedKmH, batteryVoltage);

        return location;
    }

    private static Location parseExtendedPositionalData(ByteBuffer buffer) {
        return null;
    }
}