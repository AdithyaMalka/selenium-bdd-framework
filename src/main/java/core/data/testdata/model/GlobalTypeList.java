package core.data.testdata.model;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.List;

@Builder
@Getter
@ToString
@Jacksonized
public class GlobalTypeList {
    private HashMap<String, List<String>> typeListMap;
}
