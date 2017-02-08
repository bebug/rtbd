package de.florianbuchner.trbd.entity.component;

import com.badlogic.ashley.core.Component;
import de.florianbuchner.trbd.core.FontType;

public class TextComponent implements Component {
    public String text;
    public FontType fontType;

    public TextComponent(String text, FontType fontType) {
        this.text = text;
        this.fontType = fontType;
    }
}
