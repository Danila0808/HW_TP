package Map;

import Entities.Lighthouse;

public class LightTile extends Tile{
    private Lighthouse lighthouse;

    public LightTile(int x, int y, TerrainType type, Lighthouse lighthouse){
        super(x, y, type);
        this.lighthouse = lighthouse;
    }

    public Lighthouse getLighthouse(){ return lighthouse; }
    public void setLighthouse(Lighthouse lighthouse){ this.lighthouse = lighthouse; }
}
