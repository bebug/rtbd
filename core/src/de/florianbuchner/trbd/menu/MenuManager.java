package de.florianbuchner.trbd.menu;

import de.florianbuchner.trbd.core.Resources;

import java.util.LinkedList;

/**
 * Handles multi layered menus
 */
public class MenuManager implements MenuCloseHandler {

    private LinkedList<Menu> menus = new LinkedList <Menu>();

    private LinkedList<Menu> menusToAdd = new LinkedList <Menu>();

    private LinkedList<Menu> menusToRemove = new LinkedList <Menu>();

    private Resources resources;

    public MenuManager(Resources resources) {
        this.resources = resources;
    }

    public void clear() {
        if (!this.menus.isEmpty()) {
            this.menusToRemove.addAll(this.menus);
        }
    }

    public void add(Menu menu) {
        menu.setMenuCloseHandler(this);
        this.menusToAdd.add(menu);
    }

    public void render(float deltaTime) {
        for (Menu menu : menusToAdd) {
            this.menus.add(menu);
        }
        menusToAdd.clear();
        for (Menu menu : menusToRemove) {
            if (this.menus.contains(menu)) {
                this.menus.remove(menu);
            }
        }
        menusToRemove.clear();
        this.resources.spriteBatch.begin();
        for (Menu menu : menus) {
            menu.render(this.resources.spriteBatch, deltaTime);
        }
        this.resources.spriteBatch.end();

    }

    /**
     * Closes the active menu
     */
    @Override
    public void onMenuClosed(Menu menu) {
        this.menus.remove(menu);
    }

    public void closeActive() {
        if (!this.menus.isEmpty()) {
            this.menus.getLast().close();
        }
    }

    public boolean menuOpen() {
        return !this.menus.isEmpty();
    }

}
