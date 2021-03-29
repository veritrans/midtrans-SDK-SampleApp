//
//  ResultViewController.swift
//  MidSwiftSample1
//
//  Created by Muhammad Fauzi Masykur on 26/08/20.
//  Copyright © 2020 Muhammad Fauzi Masykur. All rights reserved.
//

import UIKit

class ResultViewController: UIViewController {

    @IBOutlet weak var paymentStatusLabel: UILabel!
    var paymentStatus = ""
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.paymentStatusLabel.text = self.paymentStatus
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
