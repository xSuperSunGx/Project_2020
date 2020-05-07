package net.project_2020.client.utils;

public class ErrorInit {

    private String header;
    private String text;

    public ErrorInit(String header, String text) {
        this.header = header;
        this.text = text;
    }

    public ErrorInit() {
    }

    public String getHeader() {
        return header;
    }

    public String getText() {
        return text;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setText(String text) {
        this.text = text;
    }
}
