<?php

namespace App\Services\Impl;

use App\Repository\CommandeRepository;
use App\Services\DashboardServiceInterface;

class DashboardService implements DashboardServiceInterface
{
    private CommandeRepository $commandeRepository;
    public function __construct(CommandeRepository $commandeRepository)
    {
        $this->commandeRepository = $commandeRepository;
    }
    public function getCommandesEnCours(): ?int
    {
        $qb = $this->commandeRepository->createQueryBuilder('c');
        $qb->select('COUNT(c.id)')
            ->where('c.etat = :etat')
            ->andWhere('c.date_commande >= :today')
            ->setParameter('etat', 'en_cours')
            ->setParameter('today', new \DateTimeImmutable('today'));
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
    public function getCommandesValidees(): ?int
    {
        $qb = $this->commandeRepository->createQueryBuilder('c');
        $qb->select('COUNT(c.id)')
            ->where('c.etat = :etat')
            ->andWhere('c.date_commande >= :today')
            ->setParameter('etat', 'terminee')
            ->setParameter('today', new \DateTimeImmutable('today'));
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
    public function getCommandesAnnulees(): ?int
    {
        $qb = $this->commandeRepository->createQueryBuilder('c');
        $qb->select('COUNT(c.id)')
            ->where('c.etat = :etat')
            ->andWhere('c.date_commande >= :today')
            ->setParameter('etat', 'annulee')
            ->setParameter('today', new \DateTimeImmutable('today'));
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
    public function getLastCommandes(): array
    {
        return $this->commandeRepository->findBy(
            [],
            ['date_commande' => 'DESC'],
            5
        );
    }
    public function getRecettes(): ?float
    {
        return $this->commandeRepository->getTotalRecettes();
    }
}
