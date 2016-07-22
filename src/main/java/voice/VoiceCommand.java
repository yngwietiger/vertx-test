package voice;

public class VoiceCommand {

    private String sessionId;
    private String command;

    public VoiceCommand(String sessionId, String command) {
        this.sessionId = sessionId;
        this.command = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "VoiceCommand{" +
                "sessionId='" + sessionId + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
