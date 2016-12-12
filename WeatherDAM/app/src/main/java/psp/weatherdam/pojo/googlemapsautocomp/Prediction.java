package psp.weatherdam.pojo.googlemapsautocomp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prediction {

    private String description;
    private String id;
    private List<MatchedSubstring> matchedSubstrings = null;
    private String place_id;
    private String reference;
    private StructuredFormatting structuredFormatting;
    private List<Term> terms = null;
    private List<String> types = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The matchedSubstrings
     */
    public List<MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    /**
     * 
     * @param matchedSubstrings
     *     The matched_substrings
     */
    public void setMatchedSubstrings(List<MatchedSubstring> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    /**
     * 
     * @return
     *     The place_id
     */
    public String getPlace_id() {
        return place_id;
    }

    /**
     * 
     * @param place_id
     *     The place_id
     */
    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    /**
     * 
     * @return
     *     The reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * 
     * @param reference
     *     The reference
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * 
     * @return
     *     The structuredFormatting
     */
    public StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    /**
     * 
     * @param structuredFormatting
     *     The structured_formatting
     */
    public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }

    /**
     * 
     * @return
     *     The terms
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     * 
     * @param terms
     *     The terms
     */
    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    /**
     * 
     * @return
     *     The types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * 
     * @param types
     *     The types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
