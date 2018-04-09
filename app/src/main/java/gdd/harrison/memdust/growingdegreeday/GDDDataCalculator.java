package gdd.harrison.memdust.growingdegreeday;

class GDDDataCalculator {

    double calculateBlackLayer(double maturityValue){
        return (24.16* maturityValue) - 15.388;
    }

    double calculateSilkLayer(double maturityValue){
        return (11.459*maturityValue) +100.27;
    }

    double calculateV2Layer(){
        return 105.0 + 168.0;
    }

    double calculateVLayer(double previousV){
        return previousV + 168.0;
    }

}
