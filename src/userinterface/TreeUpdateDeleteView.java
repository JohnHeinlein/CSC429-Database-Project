package userinterface;

import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import model.Tree;
import utilities.Debug;

import java.util.Properties;

public class TreeUpdateDeleteView extends View {
    private Tree tree;

    public TreeUpdateDeleteView(IModel model) {
        super(model, "TreeUpdateDeleteView");

        setTitle("Update or Delete a Tree");

        addContent("Barcode",
                makeField("Barcode"));


        footButt(makeButt("Update",e->{
            getTree();
            myModel.stateChangeRequest("TreeUpdate",tree);
        }));

        footButt(makeButt("Delete",e->{
            getTree();
            myModel.stateChangeRequest("TreeDelete",tree);
        }));

        cancelButton();
    }

    public void getTree(){
        scrapeFields();

        String barcode = (String)props.get("barcode");

        if(barcode == null){
            Debug.logErr("No barcode retrieved");
            return;
        }
        try {
            tree = new Tree(barcode);
        }catch(InvalidPrimaryKeyException ex){
            Debug.logErr(String.format("(%s) Invalid barcode", barcode));
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
