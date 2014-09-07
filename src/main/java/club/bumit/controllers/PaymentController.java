package club.bumit.controllers;

import com.simplify.payments.PaymentsApi;
import com.simplify.payments.PaymentsMap;
import com.simplify.payments.domain.Payment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ichatz on 9/7/14.
 */
@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String showForm(final Model model) {
        return "payment";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String process(@RequestParam final String simplifyToken, final Model model)
            throws Exception {
        PaymentsApi.PRIVATE_KEY = "h6nIxIuGUx0LH1GtETYSn1+vWhnf01p5E7Oywitxfql5YFFQL0ODSXAOkNtXTToq";
        PaymentsApi.PUBLIC_KEY = "sbpb_OGY5OGZiYjEtMjMyZC00MGVlLWI0OGQtZThiNjM1OGMwYmZk";

        System.out.println(simplifyToken);

        final Payment payment = Payment.create(new PaymentsMap()
                .set("currency", "USD")
                .set("token", simplifyToken) // returned by JavaScript call
                .set("amount", 1000) // In cents e.g. $10.00
                .set("description", "BumIt Club"));
        if ("APPROVED".equals(payment.get("paymentStatus"))) {
            System.out.println("Payment approved");
        }

        model.addAttribute("paymentStatus", payment.get("paymentStatus"));
        return "payment-success";
    }
}
