package com.curtesmalteser.jsoncurrencies.utilities;

import com.curtesmalteser.jsoncurrencies.R;
import com.curtesmalteser.jsoncurrencies.model.FlagModel;

/**
 * Created by António "Curtes Malteser" Bastião on 04/02/2018.
 */

public class FlagUtils {

    public static FlagModel selectFlag(String currency) {
        FlagModel model = new FlagModel();

        switch (currency) {
            case ("EUR"):
                model.setSign(R.drawable.ic_euro_sign_black);
                model.setFlag(R.drawable.ic_euro_flag);
                break;

            case ("AUD"):
                model.setSign(R.drawable.ic_dollar_sign);
                model.setFlag(R.drawable.ic_aud_flag);
                break;

            case ("BGN"):
                model.setSign(R.drawable.ic_bgn_sign);
                model.setFlag(R.drawable.ic_bgn_flag);
                break;

            case ("BRL"):
                model.setSign(R.drawable.ic_brl_sign);
                model.setFlag(R.drawable.ic_brl_flag);
                break;

            case ("CAD"):
                model.setSign(R.drawable.ic_cad_sign);
                model.setFlag(R.drawable.ic_cad_flag);
                break;

            case ("CHF"):
                model.setSign(R.drawable.ic_chf_sign);
                model.setFlag(R.drawable.ic_chf_flag);
                break;

            case ("CNY"):
                model.setSign(R.drawable.ic_cny_sign);
                model.setFlag(R.drawable.ic_cny_flag);
                break;

            case ("CZK"):
                model.setSign(R.drawable.ic_czk_sign);
                model.setFlag(R.drawable.ic_czk_flag);
                break;

            case ("DKK"):
                model.setSign(R.drawable.ic_krone_sign);
                model.setFlag(R.drawable.ic_dkk_flag);
                break;

            case ("GBP"):
                model.setSign(R.drawable.ic_gbp_sign);
                model.setFlag(R.drawable.ic_gbp_flag);
                break;

            case ("HKD"):
                model.setSign(R.drawable.ic_hkd_dollar);
                model.setFlag(R.drawable.ic_hkd_flag);
                break;

            case ("HRK"):
                model.setSign(R.drawable.ic_hrk_sign);
                model.setFlag(R.drawable.ic_hrk_flag);
                break;

            case ("HUF"):
                model.setSign(R.drawable.ic_huf_sign);
                model.setFlag(R.drawable.ic_huf_flag);
                break;

            case ("IDR"):
                model.setSign(R.drawable.ic_idr_sign);
                model.setFlag(R.drawable.ic_idr_flag);
                break;

            case ("ILS"):
                model.setSign(R.drawable.ic_ils_sign);
                model.setFlag(R.drawable.ic_ils_flag);
                break;

            case ("INR"):
                model.setSign(R.drawable.ic_inr_sign);
                model.setFlag(R.drawable.ic_inr_flag);
                break;

            case ("ISK"):
                model.setSign(R.drawable.ic_krone_sign);
                model.setFlag(R.drawable.ic_isk_flag);
                break;

            case ("JPY"):
                model.setSign(R.drawable.ic_jpy_sign);
                model.setFlag(R.drawable.ic_jpy_flag);
                break;

            case ("KRW"):
                model.setSign(R.drawable.ic_krw_sign);
                model.setFlag(R.drawable.ic_krw_flag);
                break;

            case ("MXN"):
                model.setSign(R.drawable.ic_mxn_sign);
                model.setFlag(R.drawable.ic_mxn_flag);
                break;

            case ("MYR"):
                model.setSign(R.drawable.ic_myr_sign);
                model.setFlag(R.drawable.ic_myr_flag);
                break;

            case ("NOK"):
                model.setSign(R.drawable.ic_nok_sign);
                model.setFlag(R.drawable.ic_nok_flag);
                break;

            case ("NZD"):
                model.setSign(R.drawable.ic_dollar_sign);
                model.setFlag(R.drawable.ic_nzd_flag);
                break;

            case ("PHP"):
                model.setSign(R.drawable.ic_php);
                model.setFlag(R.drawable.ic_php_flag);
                break;

            case ("PLN"):
                model.setSign(R.drawable.ic_pln_sign);
                model.setFlag(R.drawable.ic_pln_flag);
                break;

            case ("RON"):
                model.setSign(R.drawable.ic_ron_sign);
                model.setFlag(R.drawable.ic_ron_flag);
                break;

            case ("RUB"):
                model.setSign(R.drawable.ic_rub_sign);
                model.setFlag(R.drawable.ic_rub_flag);
                break;

            case ("SEK"):
                model.setSign(R.drawable.ic_sek_sign);
                model.setFlag(R.drawable.ic_sek_flag);
                break;

            case ("SGD"):
                model.setSign(R.drawable.ic_sgd_sign);
                model.setFlag(R.drawable.ic_sgd_flag);
                break;

            case ("THB"):
                model.setSign(R.drawable.ic_thb_sign);
                model.setFlag(R.drawable.ic_thb_flag);
                break;

            case ("TRY"):
                model.setSign(R.drawable.ic_try_sign);
                model.setFlag(R.drawable.ic_try_flag);
                break;

            case ("USD"):
                model.setSign(R.drawable.ic_dollar_sign);
                model.setFlag(R.drawable.ic_usa_flag);
                break;

            case ("ZAR"):
                model.setSign(R.drawable.ic_zar_sign);
                model.setFlag(R.drawable.ic_zar_flag);
                break;

            default:
                model.setSign(R.drawable.ic_dollar_sign);
                model.setFlag(R.drawable.ic_sign_flag);
        }

        return model;
    }

}
