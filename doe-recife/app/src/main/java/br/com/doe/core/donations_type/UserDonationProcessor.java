/**
 * 
 */
package br.com.doe.core.donations_type;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import br.com.doe.R;
import br.com.doe.model.User;
import br.com.doe.core.donations_type.types.BrinquedosHandler;
import br.com.doe.core.donations_type.types.CestaBasicaHandler;
import br.com.doe.core.donations_type.types.DonationHandler;
import br.com.doe.core.donations_type.types.HigiePessoalHandler;
import br.com.doe.core.donations_type.types.LencolHandler;
import br.com.doe.core.donations_type.types.MaterialLimpezaHandler;
import br.com.doe.core.donations_type.types.MaterialPedagogicoHandler;
import br.com.doe.core.donations_type.types.OutrosHandler;
import br.com.doe.core.donations_type.types.RecursoFinanceirosHandler;
import br.com.doe.core.donations_type.types.RemedioHandler;
import br.com.doe.core.donations_type.types.RoupaHandler;
import br.com.doe.core.donations_type.types.ToalhaBanhoHandler;
import br.com.doe.core.donations_type.types.ToalhaMesa;

/**
 * @author heitornsouza
 * 
 */
public class UserDonationProcessor {

	private static UserDonationProcessor instance;

	private DonationHandler first;
	private DonationHandler successor;

	// TODO create a appropriate list to manage the list
	private ArrayList<DonationHandler> list = new ArrayList<DonationHandler>();

	public UserDonationProcessor(Context ctx) {
		createHandler(ctx);
	}

	private void addHandler(DonationHandler dh) {
		if (first == null) {
			this.first = dh;
		} else {
			this.successor.setSuccessor(dh);
		}

		this.successor = dh;
		list.add(dh);
	}

	public void count(String donationType, Context ctx, User user) {
		this.first.handle(donationType, ctx, user);
	}

	private void createHandler(Context ctx) {
		addHandler(new MaterialLimpezaHandler(R.drawable.ic_limpeza_blue, ctx));
		addHandler(new CestaBasicaHandler(R.drawable.ic_cesta_blue, ctx));
		addHandler(new HigiePessoalHandler(R.drawable.ic_higiene_blue, ctx));
		addHandler(new MaterialPedagogicoHandler(R.drawable.ic_pedagogico_blue, ctx));
		addHandler(new RemedioHandler(R.drawable.ic_remedios_blue, ctx));
		addHandler(new BrinquedosHandler(R.drawable.ic_brinquedos_blue, ctx));
		addHandler(new OutrosHandler(R.drawable.ic_outros_blue, ctx));
		addHandler(new RecursoFinanceirosHandler(R.drawable.ic_financeiro_blue, ctx));
		addHandler(new ToalhaBanhoHandler(R.drawable.ic_banho_blue, ctx));
		addHandler(new ToalhaMesa(R.drawable.ic_mesa_blue, ctx));
		addHandler(new LencolHandler(R.drawable.ic_lencol_blue, ctx));
		addHandler(new RoupaHandler(R.drawable.ic_roupa_blue, ctx));
	}

	public List<DonationHandler> getDonationHandler() {
		return this.list;
	}
}
