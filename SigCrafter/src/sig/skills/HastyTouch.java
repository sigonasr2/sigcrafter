package sig.skills;

import sig.Craft;
import sig.Skill;

public class HastyTouch extends Skill {

	public HastyTouch(String name, int CPCost, boolean guaranteed, int lvReq, int modifier, int key) {
		super(name, CPCost, guaranteed, lvReq, modifier, key);
	}

	public HastyTouch(String name, int CPCost, boolean guaranteed, int lvReq, int key) {
		super(name, CPCost, guaranteed, lvReq, key);
	}

	@Override
	public void useSkill(Craft c) {
		c.craft_quality += 1 * c.quality_mult * (0.37*c.control+32.6)*(1-0.05*Math.min(Math.max(c.recipe_level-c.level,0),5));
		c.craft_durability -= 10 * c.durability_mult;
		super.useSkill(c);
		super.updateBuffs(c);
	}

}
