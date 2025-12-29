<?php

namespace App\Controller;

use App\DTO\CommandeListDto;
use App\DTO\CommandeSearchDto;
use App\Form\CommandeSearchType;
use App\Services\Impl\CommandeService;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class CommandeController extends AbstractController
{
    private const LIMIT = 10;
    public function __construct(private CommandeService $commandeService) {}
    #[Route('/', name: 'app_commande_list', methods:['GET', 'POST'])]
    public function index(Request $request): Response
    {
        $filtre = [];
        $searchDto = new CommandeSearchDto();
        $form = $this->createForm(CommandeSearchType::class, $searchDto);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            if ($searchDto->nomClient !== null) {
                $filtre['client'] = $searchDto->nomClient;
            }
            if ($searchDto->etatCmd !== null) {
                $filtre['etat'] = $searchDto->etatCmd;
            }
            if ($searchDto->typeCmd !== null) {
                $filtre['type'] = $searchDto->typeCmd;
            }
        }
        $page = max(1, (int)$request->query->get('page', 1));
        $offset = ($page - 1) * self::LIMIT;
        $commandes = $this->commandeService->getFilteredCommandes($filtre, self::LIMIT, $offset);
        $commandesDto = CommandeListDto::toDtoArray($commandes);
        $count = $this->commandeService->countCommandes($filtre);
        $nbrPage = max(1, ceil($count / self::LIMIT));
        return $this->render('commande/index.html.twig', [
            'commandes' => $commandesDto,
            'nbrPage' => $nbrPage,
            'pageEnCours' => $page,
            'formSearch' => $form->createView()
        ]);
    }
    #[Route('/{id}', name: 'app_commande_details', requirements: ['id' => '\d+'], methods:['GET'])]
    public function show(int $id): Response
    {
        $commande = $this->commandeService->getCommandeById($id);
        if (!$commande) {
            throw $this->createNotFoundException("Commande #$id introuvable.");
        }
        return $this->render('commande/details.html.twig', [
            'commande' => $commande,
        ]);
    }
    #[Route('/{id}/annuler', name: 'app_commande_annuler', requirements: ['id' => '\d+'], methods:['GET'])]
    public function annuler(int $id): Response
    {
        $result = $this->commandeService->annulerCommande($id);
        if ($result === null) {
            $this->addFlash('danger', "Commande #$id introuvable.");
        } elseif ($result === false) {
            $this->addFlash('warning', "Impossible d'annuler une commande terminée.");
        } else {
            $this->addFlash('success', "Commande #$id annulée avec succès.");
        }
        return $this->redirectToRoute('app_commande_list');
    }
    #[Route('/{id}/terminer', name: 'app_commande_terminer', requirements: ['id' => '\d+'], methods:['GET'])]
    public function terminer(int $id): Response
    {
        $result = $this->commandeService->terminerCommande($id);
        if ($result === null) {
            $this->addFlash('danger', "Commande #$id introuvable.");
        } elseif ($result === false) {
            $this->addFlash('warning', "Impossible de terminer une commande annulée.");
        } else {
            $this->addFlash('success', "Commande #$id terminée avec succès.");
        }
        return $this->redirectToRoute('app_commande_list');
    }
}
