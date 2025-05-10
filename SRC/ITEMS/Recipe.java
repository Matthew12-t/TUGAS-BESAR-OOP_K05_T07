package SRC.ITEMS;
import java.util.List;
public class Recipe {
    private String name;
    private Food food;
    private List<Item> ingredients;
    public Recipe(String name, Food food, List<Item> ingredients) {
        this.name = name;
        this.food = food;
        this.ingredients = ingredients;
    }
    // Getters
    public String getName() {
        return name;
    }
    public Food getFood() {
        return food;
    }
    public List<Item> getIngredients() {
        return ingredients;
    }
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setFood(Food food) {
        this.food = food;
    }
    public void setIngredients(List<Item> ingredients) {
        this.ingredients = ingredients;
    }
}
