package tn.esprit.test;//package tn.esprit.test;
//
//import tn.esprit.entities.Equipe;
//import tn.esprit.entities.Partie;
//import tn.esprit.entities.TypeMatch;
//import tn.esprit.services.ServiceEquipe;
//import tn.esprit.services.ServiceMatch;
//
//import java.sql.SQLException;
//import java.util.List;
//
//public class Main {
//
//    public static void main(String[] args) {
//        // Création des instances des services
//        ServiceEquipe serviceEquipe = new ServiceEquipe();
//        ServiceMatch serviceMatch = new ServiceMatch();
//
//        try {
//                // 1. Ajouter un match
//                //System.out.println("Ajout d'un match...");
//                // Utilisation de l'enum TypeMatch au lieu de "Football"
//                Partie match = new Partie(TypeMatch.AMICAL, "1-0", java.sql.Date.valueOf("2025-02-20"), "Stade XYZ");
//                //serviceMatch.ajouter(match);
//
//                // 2. Ajouter une équipe
//                //System.out.println("Ajout d'une équipe...");
//                Equipe equipe = new Equipe("Équipe A", 1, "image.jpg", "Description de l'équipe", 100, 2);
//                //serviceEquipe.ajouter(equipe);
//            //   System.out.println(match.getIdMatch() + "  " + equipe.getIdEquipe());
//                // 3. Lier une équipe à un match
//                //System.out.println("Ajout de l'équipe au match...");
//                //match.setIdMatch(4);
//               // equipe.setIdEquipe(4);
//                //serviceEquipe.ajouterEquipeAMatch(match.getIdMatch(), equipe.getIdEquipe());
//
////               ///Afficher matchs:
////            List<Partie> matchs = serviceMatch.afficher();
////            System.out.println("\n📌 Liste des matchs enregistrés:");
////            for (Partie partie : matchs) {
////                System.out.println("🆔 ID: " + partie.getIdMatch() +
////                        " | 🏟 type: " + partie.getType() +
////                        " | 📍 resultat: " + partie.getResultat() +
////                        " | 📝 lieu: " + partie.getLieu() +
////                        " | 🖼 date: " + partie.getDateMatch());
////            }
////            System.out.println(serviceMatch.afficher());
//
////            // Afficher les équipes
////            List<Equipe> equipes = serviceEquipe.afficher();
////            System.out.println("\n📌 Liste des équipes enregistrées:");
////            for (Equipe equip : equipes) {
////                System.out.println("🆔 ID: " + equip.getIdEquipe() +
////                        " | 🏆 Nom: " + equip.getNom() +
////                        " | 🖼 Image: " + equip.getImage() +
////                        " | 📖 Description: " + equip.getDescription() +
////                        " | 🔢 Score: " + equip.getPoints() +
////                        " | 🔢 id_entraineur: " + equip.getIdEntraineur() +
////                        " | 🏅 Classement: " + equip.getClassement());
////            }
//
////                 //4. Afficher les équipes associées à un match
////                System.out.println("Les équipes du match :");
////                List<Integer> equipesMatch = serviceMatch.getEquipesParMatch(match.getIdMatch());
////                for (Integer idEquipe : equipesMatch) {
////                    System.out.println("Équipe ID: " + idEquipe);
////                }
////
////                // 5. Afficher les matchs auxquels une équipe participe
////                System.out.println("Les matchs de l'équipe :");
////                List<Integer> matchsEquipe = serviceEquipe.getMatchsParEquipe(equipe.getIdEquipe());
////                for (Integer idMatch : matchsEquipe) {
////                    System.out.println("Match ID: " + idMatch);
////                }
//
////                // 6. Modifier une équipe
////                System.out.println("Modification du nom de l'équipe...");
////                equipe.setIdEquipe(4);
////                equipe.setNom("Équipe B");
////                serviceEquipe.modifier(equipe);
//
////                // 7. Supprimer une équipe d'un match
////                System.out.println("Suppression de l'équipe du match...");
////                match.setIdMatch(4);
////                equipe.setIdEquipe(4);
////                serviceEquipe.supprimerEquipeDuMatch(match.getIdMatch(), equipe.getIdEquipe());
//
////                // 8. Supprimer un match
////                System.out.println("Suppression du match...");
////                match.setIdMatch(4);
////                serviceMatch.supprimer(match.getIdMatch());
//
////                // 9. Supprimer une équipe
////                System.out.println("Suppression de l'équipe...");
////                equipe.setIdEquipe(3);
////                serviceEquipe.supprimer(equipe.getIdEquipe());
//
//        } catch (SQLException e) {
//            System.out.println("Erreur SQL : " + e.getMessage());
//        }
//    }
//}
