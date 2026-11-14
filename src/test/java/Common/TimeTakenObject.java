package Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeTakenObject {

    private String tagName;
    private String scenarioName;
    private String timeTaken;
    private String passFail;
}