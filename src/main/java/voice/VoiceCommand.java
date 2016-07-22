package voice;

import java.util.HashMap;
import java.util.Map;

public class VoiceCommand {

    private String sessionId;
    private String command;
    private Map<String, String> parameters = new HashMap<>();

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

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "VoiceCommand{" +
                "sessionId='" + sessionId + '\'' +
                ", command='" + command + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
