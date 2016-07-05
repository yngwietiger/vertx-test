package voice;

public class VoiceCommand {

    private final String sessionId;
    private final String command;

    public VoiceCommand(String sessionId, String command) {
        this.sessionId = sessionId;
        this.command = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "VoiceCommand{" +
                "sessionId='" + sessionId + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
