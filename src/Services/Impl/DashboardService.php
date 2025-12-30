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
            ->andWhere('c.dateCommande >= :today')
            ->setParameter('etat', 'EN_COURS')
            ->setParameter('today', new \DateTimeImmutable('today'));
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
    public function getCommandesValidees(): ?int
    {
        $qb = $this->commandeRepository->createQueryBuilder('c');
        $qb->select('COUNT(c.id)')
            ->where('c.etat = :etat')
            ->andWhere('c.dateCommande >= :today')
            ->setParameter('etat', 'TERMINEE')
            ->setParameter('today', new \DateTimeImmutable('today'));
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
    public function getCommandesAnnulees(): ?int
    {
        $qb = $this->commandeRepository->createQueryBuilder('c');
        $qb->select('COUNT(c.id)')
            ->where('c.etat = :etat')
            ->andWhere('c.dateCommande >= :today')
            ->setParameter('etat', 'ANNULEE')
            ->setParameter('today', new \DateTimeImmutable('today'));
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
    public function getLastCommandes(): array
    {
        return $this->commandeRepository->findBy(
            [],
            ['dateCommande' => 'DESC'],
            5
        );
    }
    public function getRecettes(): ?float
    {
        return $this->commandeRepository->getTotalRecettes();
    }
}
