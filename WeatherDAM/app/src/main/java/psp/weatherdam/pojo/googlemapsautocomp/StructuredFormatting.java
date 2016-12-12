package psp.weatherdam.pojo.googlemapsautocomp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructuredFormatting {

    private String mainText;
    private List<MainTextMatchedSubstring> mainTextMatchedSubstrings = null;
    private String secondaryText;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The mainText
     */
    public String getMainText() {
        return mainText;
    }

    /**
     * 
     * @param mainText
     *     The main_text
     */
    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    /**
     * 
     * @return
     *     The mainTextMatchedSubstrings
     */
    public List<MainTextMatchedSubstring> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstrings;
    }

    /**
     * 
     * @param mainTextMatchedSubstrings
     *     The main_text_matched_substrings
     */
    public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstring> mainTextMatchedSubstrings) {
        this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
    }

    /**
     * 
     * @return
     *     The secondaryText
     */
    public String getSecondaryText() {
        return secondaryText;
    }

    /**
     * 
     * @param secondaryText
     *     The secondary_text
     */
    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
