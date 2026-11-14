package Common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScreenshotObject {

    private byte[] screenshotValue;
    private String mediaType;
    private String screenshotName;

}