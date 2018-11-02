/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pizzamdp;


import entidades.TipoPizza;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author busquedad
 */

public class PizzaMDP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws HibernateException {
        // TODO code application logic here
        
        SessionFactory sessionFactory =
        new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        Session session = sessionFactory.openSession();
       
        try {
       
            session.beginTransaction();
       // Address address = new Address("OMR Road", "Chennai", "TN", "600097");
        //By using cascade=all option the address need not be saved explicitly when the student object is persisted the address will be automatically saved.
            //session.save(address);
       
           /**SQL Delete = Hibernate delete*/
           /*TipoPizza tipoPizza = new TipoPizza();
           tipoPizza.setId_tipo_pizza(5);
           session.delete(tipoPizza);
           session.getTransaction().commit();
           System.out.println("registro eliminado");*/
           
            /**SQL Insert = Hibernet save*/
           /* TipoPizza tipoPizza = new TipoPizza();
            tipoPizza.setNombre("Pizza Napolitana");
            tipoPizza.setDescripcionPizza("Tomate, Muzzarella, lechuga");
            session.save(tipoPizza);
            session.getTransaction().commit();
            System.out.println("registro ingresado");*/
            
         /**SQL Select = Hibernet get*/
         /* TipoPizza tipoPizza = (TipoPizza) session.get(TipoPizza.class, 3);
            if (tipoPizza != null){
                System.out.println(tipoPizza.getDescripcionPizza());
                System.out.println(tipoPizza.getId_tipo_pizza());
                System.out.println(tipoPizza.getNombre());
            }else{
                System.out.println("No existe registro");
            }*/
         
         /**SQL Update = Hibernet get,set,update*/
             /*TipoPizza tipoPizza = (TipoPizza) session.get(TipoPizza.class, 6);
             tipoPizza.setNombre("Quatro Quesos");
             session.update(tipoPizza);
             session.getTransaction().commit();*/

        } catch (HibernateException e) {
        //  System.out.print(e.);
        } finally {
        session.close();
        }
        }
    
}
