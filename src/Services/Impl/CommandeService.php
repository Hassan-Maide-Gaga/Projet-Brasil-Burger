<?php

namespace App\Services\Impl;

use App\Entity\Commande;
use App\Entity\EtatCommande;
use App\Repository\CommandeRepository;
use App\Services\CommandeServiceInterface;
use Doctrine\ORM\EntityManagerInterface;

class CommandeService implements CommandeServiceInterface
{
    public function __construct(
        private CommandeRepository $commandeRepository,
        private EntityManagerInterface $em
    ) {}
    
    public function getAllCommandes(): array
    {
        return $this->commandeRepository->findAll();
    }
    
    public function getCommandeById(int $id): ?Commande
    {
        return $this->commandeRepository->find($id);
    }
    
    public function annulerCommande(int $id): ?bool
    {
        $commande = $this->getCommandeById($id);
        if (!$commande) {
            return null;
        }
        if ($commande->getEtat()?->value === EtatCommande::TERMINEE->value) {
            return false;
        }
        $commande->setEtat(EtatCommande::ANNULEE);
        $this->em->flush();
        return true;
    }
    
    public function terminerCommande(int $id): ?bool
    {
        $commande = $this->getCommandeById($id);
        if (!$commande) {
            return null;
        }
        if ($commande->getEtat()?->value === EtatCommande::ANNULEE->value) {
            return false;
        }
        $commande->setEtat(EtatCommande::TERMINEE);
        $this->em->flush();
        return true;
    }
    
    public function getFilteredCommandes(array $filters, int $limit, int $offset): array
    {
        $qb = $this->commandeRepository->createQueryBuilder('c')
            ->leftJoin('c.client', 'client')  
            ->orderBy('c.dateCommande', 'DESC')
            ->setFirstResult($offset)
            ->setMaxResults($limit);
            
        if (!empty($filters['client'])) {
            $qb->andWhere('LOWER(client.nomComplet) LIKE :client')  
                ->setParameter('client', '%'.strtolower($filters['client']).'%');
        }
        
        if (!empty($filters['etat'])) {
            $etat = $filters['etat'];
            if (!is_string($etat)) {
                $etat = $etat->value;
            }
            $qb->andWhere('c.etat = :etat')->setParameter('etat', $etat);
        }
        
        if (!empty($filters['type'])) {
            $type = $filters['type'];
            if (!is_string($type)) {
                $type = $type->value;
            }
            $qb->andWhere('c.type = :type')->setParameter('type', $type);
        }
        
        if (!empty($filters['date'])) {
            $qb->andWhere('DATE(c.dateCommande) = :date')
                ->setParameter('date', $filters['date']->format('Y-m-d'));
        }

        return $qb->getQuery()->getResult();
    }
    
    public function countCommandes(array $filters): int
    {
        $qb = $this->commandeRepository->createQueryBuilder('c')
            ->select('COUNT(c.id)')
            ->leftJoin('c.client', 'client'); 
            
        if (!empty($filters['client'])) {
            $qb->andWhere('LOWER(client.nomComplet) LIKE :client')  
                ->setParameter('client', '%'.strtolower($filters['client']).'%');
        }
        
        if (!empty($filters['etat'])) {
            $etat = $filters['etat'];
            if (!is_string($etat)) {
                $etat = $etat->value;
            }
            $qb->andWhere('c.etat = :etat')->setParameter('etat', $etat);
        }
        
        if (!empty($filters['type'])) {
            $type = $filters['type'];
            if (!is_string($type)) {
                $type = $type->value;
            }
            $qb->andWhere('c.type = :type')->setParameter('type', $type);
        }
        
        if (!empty($filters['date'])) {
            $qb->andWhere('DATE(c.dateCommande) = :date')
                ->setParameter('date', $filters['date']->format('Y-m-d'));
        }
        
        return (int) $qb->getQuery()->getSingleScalarResult();
    }
}