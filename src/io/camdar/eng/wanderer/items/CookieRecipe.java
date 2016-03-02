package io.camdar.eng.wanderer.items;

// CookieRecipes increase the amount of cookeis the player gains per movement.
public abstract class CookieRecipe extends Item {    
    public abstract String getDescription();
    
    // Counts how many steps have been taken.
    private int counter;
    
    // After this many steps, the number of cookies increases and counter is set
    // to zero.
    private int numStepsToIncrement;
    
    // The amount of cookies to add once numStepsToIncrement has been reached.
    private int numCookiesToAdd;
    
    // Increments the count. If it exceeds numStepsToIncrent,
    // return numCookiesToAdd.
    public int incrementCount() {
        counter++;
        if (counter >= numStepsToIncrement) {
            counter = 0;
            return numCookiesToAdd;
        }
        return 0;
    }
    
    // Cookie recipes provide no stats.
    public int attackIncrease() {
        return 0;
    }

    public int defenseIncrease() {
        return 0;
    }
    
    public int healthIncrease() {
        return 0;
    }
    
    public void setCounter(int stCnt) {
        counter = stCnt;
    }
    
    public void setNumStepsToIncrement(int NSTI) {
        numStepsToIncrement = NSTI;
    }
    
    public void setNumCookiesToAdd(int NCTA) {
        numCookiesToAdd = NCTA;
    }
}
