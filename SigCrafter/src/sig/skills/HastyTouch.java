package sig.skills;

import sig.Craft;
import sig.Skill;

public class HastyTouch extends Skill {

	public HastyTouch(String name, int CPCost, boolean guaranteed, int lvReq) {
		super(name, CPCost, guaranteed, lvReq);
	}

	@Override
	public void useSkill(Craft c) {
		super.useSkill(c);
		c.craft_quality += 1 * c.quality_mult * (0.37*c.control+32.6)*(1-0.05*Math.min(Math.max(c.recipe_level-c.level,0),5));
		c.craft_durability -= 10 * c.durability_mult;
	}

}
