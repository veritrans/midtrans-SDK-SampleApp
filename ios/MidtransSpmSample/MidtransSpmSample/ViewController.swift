//
//  ViewController.swift
//  xc-sample-swift
//
//  Created by Muhammad Fauzi Masykur on 26/08/22.
//

import UIKit
import MidtransKit

class ViewController: UIViewController, MidtransUIPaymentViewControllerDelegate {
    @IBOutlet weak var snaptokenTextField: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()

        //setup callback url, to enable customer to go back to this app after completing payment in e-wallet app (gojek/shopeepay).
        //setup gopay callback url
        MidtransConfig.shared()?.callbackSchemeURL = "sampleapp://"
        //setup shopeepay callback url
        MidtransConfig.shared()?.shopeePayCallbackURL = "sampleapp://"
        MidtransCreditCardConfig.shared()?.authenticationType = .type3DS
        
        
    }
    
    func generateRandomOrderID()-> String {
        let date = Date()
        let df = DateFormatter()
        df.dateFormat = "HHmmss"
        let randomOrderID = df.string(from: date)
        return randomOrderID
    }
    
    func showSnaptokenEmptyAlert() {
        let alert = UIAlertController(title: "Snaptoken is empty", message: "please input a snaptoken to use a snaptokenflow payment", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .cancel, handler: nil))
        self.present(alert, animated: true)
    }
    
    func showPaymentStatusPage(paymentStatus:String){
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "resultVC") as! ResultViewController
        vc.paymentStatus = paymentStatus
        self.navigationController?.pushViewController(vc, animated: true)
    }

    @IBAction func payWithSnaptokenFlow(_ sender: Any) {
        if snaptokenTextField.text?.count != 0 {
            MidtransMerchantClient.shared().requestTransacation(withCurrentToken: snaptokenTextField.text!) { (response, error) in
                if (response != nil){
                    //present payment page
                    let vc = MidtransUIPaymentViewController.init(token: response)
                    vc?.paymentDelegate = self
                    self.present(vc!, animated: true, completion: nil)
                } else {
                    print("error \(error!)");
                }
            }
        } else {
            self.showSnaptokenEmptyAlert()
        }
    }
    
    @IBAction func payWithSDKFlow(_ sender: Any) {
        
        //Create transaction details
        let itemJacket = MidtransItemDetail.init(itemID: "item001", name: "swift jacket", price: 100, quantity: 1)
        let itemPants = MidtransItemDetail.init(itemID: "item002", name: "swift pants", price: 100, quantity: 1)
        let address = MidtransAddress(firstName: "cihuy", lastName: "asik", phone: "081231231", address: "jalan buntu", city: "jakarta", postalCode: "12312", countryCode: "IDN")
        let customerDetail = MidtransCustomerDetails.init(firstName: "cihuy", lastName: "asik", email: "cihuy@cihuyhuy.com", phone: "08123123123", shippingAddress: address, billingAddress: address)
        let transactionDetail = MidtransTransactionDetails.init(orderID: self.generateRandomOrderID(), andGrossAmount: 200)

        MidtransMerchantClient.shared().requestTransactionToken(with: transactionDetail!, itemDetails: [itemJacket!, itemPants!], customerDetails: customerDetail) { (response, error) in
            if (response != nil) {
                //present payment page
                let vc = MidtransUIPaymentViewController.init(token: response)
                vc?.paymentDelegate = self
                self.present(vc!, animated: true, completion: nil)
            }
            else {
                print("error \(error!)")
            }
        }
    }

    //MARK: - MidtransUIPaymentViewControllerDelegate
    //These are the 5 mandatory transaction callback/delegates that need to be implemented to handle all the transaction status
    func paymentViewController(_ viewController: MidtransUIPaymentViewController!, paymentPending result: MidtransTransactionResult!) {
        print("payment pending")
        self.showPaymentStatusPage(paymentStatus: result.transactionStatus)
    }
    
    func paymentViewController(_ viewController: MidtransUIPaymentViewController!, paymentSuccess result: MidtransTransactionResult!) {
        print("payment success")
        self.showPaymentStatusPage(paymentStatus: result.transactionStatus)
    }
    
    func paymentViewController(_ viewController: MidtransUIPaymentViewController!, paymentFailed error: Error!) {
        print("payment failed")
        self.showPaymentStatusPage(paymentStatus: "failed")
    }
    
    func paymentViewController_paymentCanceled(_ viewController: MidtransUIPaymentViewController!) {
        print("payment cancelled")
        self.showPaymentStatusPage(paymentStatus: "cancelled")
    }
    
    func paymentViewController(_ viewController: MidtransUIPaymentViewController!, paymentDeny result: MidtransTransactionResult!) {
        print("payment denied")
        showPaymentStatusPage(paymentStatus: result.transactionStatus)
    }

}

