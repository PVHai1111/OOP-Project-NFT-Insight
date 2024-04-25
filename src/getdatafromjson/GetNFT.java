package getdatafromjson;

import java.util.ArrayList;

import modal.NFT;

public abstract class GetNFT<T extends NFT> {

	public abstract ArrayList<T> getArrayList(String path);
}
