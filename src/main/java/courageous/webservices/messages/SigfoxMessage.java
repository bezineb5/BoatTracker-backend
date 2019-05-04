package courageous.webservices.messages;

public class SigfoxMessage {
    private String device;
    private long time;
    private boolean duplicate;
    private double snr;
    private String station;
    private String data;
    private double avgSnr;
    private double lat;
    private double lng;
    private double rssi;
    private long seqNumber;
    private int ack;

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isDuplicate() {
        return this.duplicate;
    }

    public boolean getDuplicate() {
        return this.duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public double getSnr() {
        return this.snr;
    }

    public void setSnr(double snr) {
        this.snr = snr;
    }

    public String getStation() {
        return this.station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getAvgSnr() {
        return this.avgSnr;
    }

    public void setAvgSnr(double avgSnr) {
        this.avgSnr = avgSnr;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getRssi() {
        return this.rssi;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public long getSeqNumber() {
        return this.seqNumber;
    }

    public void setSeqNumber(long seqNumber) {
        this.seqNumber = seqNumber;
    }

    public int getAck() {
        return this.ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }
}