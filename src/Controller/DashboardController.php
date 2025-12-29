<?php

namespace App\Controller;

use App\Services\DashboardServiceInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class DashboardController extends AbstractController
{
    public function __construct(
        private DashboardServiceInterface $dashboardService
    ) {}

    #[Route('/', name: 'app_dashboard')]
    public function index(): Response
    {
        return $this->render('dashboard/index.html.twig', [
            'enCours'     => $this->dashboardService->getCommandesEnCours(),
            'validees'    => $this->dashboardService->getCommandesValidees(),
            'annulees'    => $this->dashboardService->getCommandesAnnulees(),
            'recettes'    => $this->dashboardService->getRecettes(),
            'commandes'   => $this->dashboardService->getLastCommandes(),
        ]);
    }
}
