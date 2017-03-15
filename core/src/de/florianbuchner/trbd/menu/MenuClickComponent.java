package de.florianbuchner.trbd.menu;

import com.badlogic.gdx.math.Rectangle;

public class MenuClickComponent {
    public Rectangle rectangle;
    public MenuClickHanlder menuClickHanlder;

    public MenuClickComponent(Rectangle rectangle, MenuClickHanlder menuClickHanlder) {
        this.rectangle = rectangle;
        this.menuClickHanlder = menuClickHanlder;
    }

    public interface MenuClickHanlder {
        void onClick();
    }
}
