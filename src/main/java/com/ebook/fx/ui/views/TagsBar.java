package com.ebook.fx.ui.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;

/**
 *
 * @author maykoone
 */
public class TagsBar extends FlowPane {

    private ObservableList<String> tags;
    private static final String DEFAULT_STYLE_CLASS = "tags-bar";
    private static final String DEFAULT_PROMPT_TEXT = "Add a tag...";
    private TextField tagsInput;

    public TagsBar(ObservableList<String> tags) {
        this.tags = tags;

        this.getStyleClass().addAll(DEFAULT_STYLE_CLASS);

        tagsInput = new TextField();
        tagsInput.setPromptText(DEFAULT_PROMPT_TEXT);
        tagsInput.setOnKeyPressed(evt -> {
            if (evt.getCode() == KeyCode.TAB || evt.getCode() == KeyCode.SEMICOLON) {
                String text = tagsInput.getText();
                if (!text.isEmpty()) {
                    addTag(text);
                }
                tagsInput.clear();
                tagsInput.requestFocus();
            }
        });
        this.setTags(tags);
        this.getChildren().add(tagsInput);
    }

    public TagsBar() {
        this(FXCollections.observableArrayList());
    }

    public void addTag(String tag) {
        if (!hasTag(tag)) {
            final int size = getChildren().size();
            Label iconLabel = new Label(null, new ImageView(getClass().getResource("/x16.png").toExternalForm()));

            Label tagLabel = new Label(tag, iconLabel);
            tagLabel.setContentDisplay(ContentDisplay.RIGHT);
            tagLabel.getStyleClass().add("tag");

            iconLabel.setOnMouseClicked(e -> removeTag(tagLabel));
            if (size > 0) {
                getChildren().add(size - 1, tagLabel);
            } else {
                getChildren().add(tagLabel);
            }
            tags.add(tag);
        }
    }

    public boolean hasTag(String tag) {
        return getChildren().stream()
                .filter(n -> n instanceof Label)
                .map(n -> (Label) n)
                .anyMatch(n -> n.getText().equalsIgnoreCase(tag));
    }

    private void removeTag(Label tagLabel) {
        getChildren().remove(tagLabel);
        tags.remove(tagLabel.getText());
    }

    public ObservableList<String> getTags() {
        return tags;
    }

    public void setTags(ObservableList<String> tags) {
        tags.forEach(tag -> addTag(tag));
    }
//
//    static class Tag {
//        private String text;
//    }

}
