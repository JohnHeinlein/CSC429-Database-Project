package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import model.Tree;
import utilities.Debug;

public class TreeUpdateDeleteView extends View {

    public TreeUpdateDeleteView(IModel model) {
        super(model, "TreeUpdateDeleteView");

        setTitle("Update or Delete a Tree");

        addContent("Barcode",
                makeField("Barcode"));


        footButt(makeButt("Update",e->{
            getTree();
            myModel.stateChangeRequest("TreeUpdate",getTree());
        }));

        footButt(makeButt("Delete",e->{
            getTree();
            myModel.stateChangeRequest("TreeDelete",getTree());
        }));

        cancelButton();
    }

    public Tree getTree(){
        scrapeFields();

        String barcode = (String)props.get("barcode");

        if(barcode == null){
            Debug.logErr("No barcode retrieved");
            return null;
        }
        try {
            return new Tree(barcode);
        }catch(InvalidPrimaryKeyException ex){
            Debug.logErr(String.format("(%s) Invalid barcode", barcode));
        }
        return null;
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
