package com.example.bmi.Model;

import java.util.List;

public class Root {

    private String text;

    private List<Parsed> parsed;

    private List<Hints> hints;

    private _links _links;

    public Root(String text, List<Parsed> parsed, List<Hints> hints, com.example.bmi.Model._links _links) {
        this.text = text;
        this.parsed = parsed;
        this.hints = hints;
        this._links = _links;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Parsed> getParsed() {
        return parsed;
    }

    public void setParsed(List<Parsed> parsed) {
        this.parsed = parsed;
    }

    public List<Hints> getHints() {
        return hints;
    }

    public void setHints(List<Hints> hints) {
        this.hints = hints;
    }

    public com.example.bmi.Model._links get_links() {
        return _links;
    }

    public void set_links(com.example.bmi.Model._links _links) {
        this._links = _links;
    }
}
