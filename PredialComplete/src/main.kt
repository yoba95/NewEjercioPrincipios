import java.text.DecimalFormat
import java.time.LocalDate
import java.time.Month

class Person(val fullname: String,
             val birtDate: LocalDate,
             val genre: String,
             val isSingle: Boolean
){
    fun getAge(): Int{
        return (LocalDate.now().year-this.birtDate.year)
    }
}

interface IZone{
    val key:String
    val zone: String
    val cost:Double
}

data class RuralZone( override val key: String,
                      override val zone: String,
                      override val cost: Double
):IZone

data class UrbanZone( override val key: String,
                      override val zone: String,
                      override val cost: Double
):IZone

data class ResidencialZone( override val key: String,
                      override val zone: String,
                      override val cost: Double
):IZone
data class MarginalZone( override val key: String,
                      override val zone: String,
                      override val cost: Double
):IZone

class Property(
    val extension: Double,
    val zone: IZone
){
    fun tax():Double{
        return (extension * this.zone.cost)
    }
}

class Tax private constructor(private val folio: Int,private val payMetDate: LocalDate,private val properties: List<Property>,private val ower: Person ){
    data class Builder(val folio: Int,val payMetDate: LocalDate,val ower: Person){
        private val properties= ArrayList<Property>()
        fun addProp(property: Property): Builder{
            properties.add(property)
            return this
        }
        fun build() = Tax (folio,payMetDate,properties,ower)
    }
    fun totalTax():Double{
        var total = properties.map {
            it.tax()
        }.sum()


        if (ower.getAge()>=70 || ower.isSingle){
            total= when(payMetDate.month){
                Month.JANUARY, Month.FEBRUARY -> total * 0.70
                else -> total * 0.50
            }
        }
        else if (payMetDate.month<=Month.FEBRUARY){
            total * 60
        }
        return  total
    }
}

fun main(){
    val person = Person("mario yobany", LocalDate.parse("2000-12-24"),"M",false)
    val zones = listOf<IZone>(
        MarginalZone("mar","marginal",2.00),
        ResidencialZone("res","residencial",8.00),
        UrbanZone("urb","urbana",10.00)
    )
    val texBuilde = Tax.Builder(folio = 1,payMetDate = LocalDate.now(),ower = person)
        .addProp(Property(1200.0,zones[1]))
        .addProp(Property(500.0,zones[3]))
        .build()
    val format = DecimalFormat("#,###.00")
    print("El impuesto a pagar de : ${person.fullname} es de: $${format.format(texBuilde.totalTax())}")

}



